package org.test.editor.infra.service;

import lombok.RequiredArgsConstructor;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import org.test.editor.core.dto.CreateFileDTO;
import org.test.editor.core.dto.FileDTO;
import org.test.editor.core.dto.UpdateFileDTO;
import org.test.editor.core.exception.ResourceNotFoundException;
import org.test.editor.core.mapper.FileMapper;
import org.test.editor.core.model.File;
import org.test.editor.core.model.FileVersion;
import org.test.editor.core.service.FileService;
import org.test.editor.core.service.FileVersionService;
import org.test.editor.core.service.storage.file.FileStorageService;
import org.test.editor.core.service.storage.folder.FolderStorageService;
import org.test.editor.infra.repository.FileRepository;
import org.test.editor.infra.repository.FolderRepository;
import org.test.editor.infra.repository.ProjectRepository;
import org.test.editor.infra.repository.UserRepository;

import org.test.editor.infra.service.storage.file.LocalFileStorageService;
import org.test.editor.util.PathHelper;
import org.test.editor.util.constant.CollaboratorRole;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import static org.test.editor.util.PathHelper.*;

@Service
@RequiredArgsConstructor
public class FileServiceImpl implements FileService {

    private final FileRepository fileRepository;
    private final FileMapper fileMapper;
    private final FileStorageService localFileStorageService;
    private final FolderStorageService localFolderStorageService;
    private final FileVersionService fileVersionService;
    private final FolderRepository folderRepository;
    private final UserRepository userRepository;
    private final GitService gitService;
    private final ConcurrentHashMap<Integer, ReentrantLock> fileLocks = new ConcurrentHashMap<>();
    private final ProjectRepository projectRepository;
    private final int OPERATION_MAX_LENGTH=2_000;

    @Transactional(isolation = Isolation.SERIALIZABLE)
    public void editFileByUser(UpdateFileDTO updateFileDTO, Integer userId) {
        ReentrantLock fileLock = fileLocks.computeIfAbsent(updateFileDTO.fileId(), id -> new ReentrantLock());
        fileLock.lock();
        var file = fileRepository.findById(updateFileDTO.fileId())
                .orElseThrow(() -> new ResourceNotFoundException("file not found"));
        try {
            var newVersion = file.getLastVersionNumber() + 1;
            var fileVersion=FileVersion.builder()
                    .fileId(file.getFileId())
                    .versionNumber(newVersion)
                    .build();
            if(updateFileDTO.content().length()>OPERATION_MAX_LENGTH)
            {
                var fileVersionPath=constructFileVersionPath(file.getVersionPath(),file.getFileName());
                localFileStorageService.createAndWriteToFile(fileVersionPath, updateFileDTO.content());
                fileVersion.setPath(fileVersionPath);
            }
           else{
               fileVersion.setDiffContent(updateFileDTO.content());
            }
            fileVersionService.createFileVersion(fileVersion,userId);
            file.setLastVersionNumber(newVersion);
            fileRepository.save(file);
            localFileStorageService.edit(file.getFilePath(), updateFileDTO.content());
            //gitService.addFiles(PathHelper.storageLocation.resolve(file.getFilePath()).toString(),"");
        } catch (IOException e ) {
            throw new RuntimeException(e.getMessage());
        } finally {
            fileLock.unlock();
        }

    }

    @Transactional(isolation = Isolation.SERIALIZABLE)
    public File createFileByUser(CreateFileDTO createFileDTO, Integer userId) {
        var folder = folderRepository.findById(createFileDTO.folderId())
                .orElseThrow(() -> new ResourceNotFoundException("folder not found for id: " + createFileDTO.folderId()));
        var project = projectRepository.findById(folder.getProjectId())
                .orElseThrow(() -> new ResourceNotFoundException("folder not found for id: " + createFileDTO.folderId()));
        var filePath = constructFilePath(folder.getFolderPath(), createFileDTO.fileName());
        var fileVersionPath=constructFileVersionsPath(project.getVersionPath(),createFileDTO.fileName());
        try {
            File file = fileMapper.toEntity(createFileDTO);
            file.setFilePath(filePath);
            file.setVersionPath(fileVersionPath);
            File savedFile = fileRepository.save(file);
            FileVersion fileVersion = fileVersionService
                    .createFileVersion(FileVersion.builder()
                            .fileId(savedFile.getFileId())
                            .versionNumber(1)
                            .build(), userId);
            localFileStorageService.createFile(file.getFileName(), folder.getFolderPath());
            localFolderStorageService.create(fileVersionPath);
            return savedFile;
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public List<FileDTO> getAllFiles() {
        return fileMapper.toDTOList(fileRepository.findAll());
    }

    public String getFileById(Integer fileId) {
        File file = fileRepository.findById(fileId)
                .orElseThrow(() -> new ResourceNotFoundException("File not found"));
        try {
            var fullPath = file.getFilePath();
            var content = localFileStorageService.read(fullPath);
            return content;
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Transactional(isolation = Isolation.SERIALIZABLE)
    public void deleteFile(Integer fileId) {
        var file = fileRepository.findById(fileId)
                .orElseThrow(() -> new ResourceNotFoundException("File not found"));
        var folder = folderRepository.findById(file.getFolderId())
                .orElseThrow(() -> new ResourceNotFoundException("Folder not found"));
        var owner = userRepository.findUserByProjectIdAndRoleId(folder.getProjectId(), CollaboratorRole.OWNER.getValue())
                .orElseThrow(() -> new ResourceNotFoundException("Owner not found"));
        var archivePath = constructProjectArchivePath(owner.getName(), folder.getProject().getProjectName()) + "/" + file.getFileName();
        var newArchivePath = generateNewArchivePath(archivePath);
        var filePath = file.getFilePath();
        try {
            file.setFilePath(archivePath);
            fileRepository.save(file);
            fileRepository.deleteById(fileId);
            moveToArchive(filePath, newArchivePath);
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    private void moveToArchive(String filePath, String newArchivePath) throws IOException {
        localFileStorageService.move(filePath, newArchivePath);
    }

}
