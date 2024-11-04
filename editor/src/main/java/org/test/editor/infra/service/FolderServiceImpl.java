package org.test.editor.infra.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import org.test.editor.core.dto.CreateFolderDTO;
import org.test.editor.core.dto.CreatePrimaryFolderDTO;
import org.test.editor.core.dto.FolderDTO;
import org.test.editor.core.exception.ResourceNotFoundException;
import org.test.editor.core.mapper.FolderMapper;
import org.test.editor.core.model.Folder;
import org.test.editor.core.service.FolderService;
import org.test.editor.core.service.storage.file.FileStorageService;
import org.test.editor.core.service.storage.folder.FolderStorageService;
import org.test.editor.infra.repository.FileRepository;
import org.test.editor.infra.repository.FolderRepository;
import org.test.editor.infra.repository.ProjectRepository;
import org.test.editor.infra.repository.UserRepository;
import org.test.editor.infra.service.storage.file.LocalFileStorageService;
import org.test.editor.infra.service.storage.folder.LocalFolderStorageService;
import org.test.editor.util.constant.CollaboratorRole;

import java.io.IOException;
import java.util.Optional;

import static org.test.editor.util.PathHelper.*;

@RequiredArgsConstructor
@Service
public class FolderServiceImpl implements FolderService {

    private final FileRepository fileRepository;
    private final FolderStorageService localFolderStorageService;
    private final FolderRepository folderRepository;
    private final FolderMapper folderMapper;
    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;

    @Transactional(isolation = Isolation.SERIALIZABLE)
    public FolderDTO createPrimaryFolder(CreatePrimaryFolderDTO createPrimaryFolderDTO) {
        var project = projectRepository.findById(createPrimaryFolderDTO.projectId())
                .orElseThrow(() -> new ResourceNotFoundException("Project not found for id: " + createPrimaryFolderDTO.projectId()));
        String folderPath = constructFolderPath(project.getProjectPath(), createPrimaryFolderDTO.folderName());
        var folder = folderMapper.toEntity(createPrimaryFolderDTO);
        folder.setFolderPath(folderPath);
        folder.setProjectId(project.getProjectId());
        return folderMapper.toDTO(createAndSaveFolder(folder));
    }

    @Transactional(isolation = Isolation.SERIALIZABLE)
    public FolderDTO createFolder(CreateFolderDTO createFolderDTO) {
        var parentFolder = folderRepository.findById(createFolderDTO.parentFolderId())
                .orElseThrow(() -> new ResourceNotFoundException("Parent folder not found for id: " + createFolderDTO.parentFolderId()));
        String folderPath = constructFolderPath(parentFolder.getFolderPath(), createFolderDTO.folderName());
        var folder = folderMapper.toEntity(createFolderDTO);
        folder.setFolderPath(folderPath);
        folder.setProjectId(parentFolder.getProjectId());
        return folderMapper.toDTO(createAndSaveFolder(folder));
    }


    private Folder createAndSaveFolder(Folder folder) {
        try {
            Folder savedFolder = folderRepository.save(folder);
            localFolderStorageService.create(folder.getFolderPath());
            return savedFolder;
        } catch (IOException e) {
            throw new RuntimeException("Failed to create folder in storage: " + e.getMessage(), e);
        }
    }



    public Optional<Folder> getFolderById(Integer folderId) {
        return folderRepository.findById(folderId);
    }


    @Transactional(isolation = Isolation.SERIALIZABLE)
    public void delete(Integer folderId) {
        var folder = folderRepository.findById(folderId)
                .orElseThrow(() -> new RuntimeException("Folder not found for id: " + folderId));
        if (folder.getParentFolderId() == null) {
            deletePrimaryFolder(folder);
        } else {
            deleteFolder(folder);
        }
    }



    public void deleteFolder(Folder folder) {
        var parentFolder = folderRepository.findById(folder.getParentFolderId())
                .orElseThrow(() -> new ResourceNotFoundException("Parent folder not found for id: " + folder.getParentFolderId()));
        var projectId = parentFolder.getProjectId();
        var owner=userRepository.findUserByProjectIdAndRoleId(projectId, CollaboratorRole.OWNER.getValue()).
                orElseThrow(() -> new RuntimeException("Owner not found"));;
        handleFolderDeletion(folder, parentFolder.getProject().getProjectName(), owner.getName());
    }


    public void deletePrimaryFolder(Folder folder) {
        var project = projectRepository.findById(folder.getProjectId())
                .orElseThrow(() -> new ResourceNotFoundException("Project not found for id: " + folder.getProjectId()));
        var owner=userRepository.findUserByProjectIdAndRoleId(project.getProjectId(), CollaboratorRole.OWNER.getValue()).
                orElseThrow(() -> new RuntimeException("Owner not found"));;
        handleFolderDeletion(folder, project.getProjectName(), owner.getName());
    }


    private void handleFolderDeletion(Folder folder, String projectName, String username) {
        String archivePath = constructProjectArchivePath(username, projectName) + "/" + folder.getFolderName();
        String newArchivePath = generateNewArchivePath(archivePath);
        String folderPath = folder.getFolderPath();
        try {
            moveToArchive(folderPath, newArchivePath);
            folder.setFolderPath(newArchivePath);
            folderRepository.save(folder);
            fileRepository.softDeleteByFolderId(folder.getFolderId(), folderPath, newArchivePath);
            folderRepository.softDeleteByFolderId(folder.getFolderId(), folderPath, newArchivePath);
        } catch (IOException e) {
            rollbackFromArchive(newArchivePath, folderPath);
            throw new RuntimeException("Failed to delete folder: " + e.getMessage(), e);
        }
    }

    private void moveToArchive(String folderPath, String newArchivePath) throws IOException {
        localFolderStorageService.move(folderPath, newArchivePath);
    }

    private void rollbackFromArchive(String newArchivePath, String folderPath) {
        try {
            localFolderStorageService.move(newArchivePath, folderPath);
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        }
    }


}