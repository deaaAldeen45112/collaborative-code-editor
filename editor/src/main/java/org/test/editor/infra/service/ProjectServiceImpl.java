package org.test.editor.infra.service;


import lombok.AllArgsConstructor;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import org.test.editor.core.dto.*;
import org.test.editor.core.exception.ResourceNotFoundException;
import org.test.editor.core.mapper.ProjectMapper;
import org.test.editor.core.model.Collaborator;
import org.test.editor.core.model.File;
import org.test.editor.core.model.Folder;
import org.test.editor.core.service.ProjectService;
import org.test.editor.infra.repository.FileRepository;
import org.test.editor.infra.repository.FolderRepository;
import org.test.editor.infra.repository.ProjectRepository;
import org.test.editor.infra.service.storage.file.LocalFileStorageService;
import org.test.editor.infra.service.storage.folder.LocalFolderStorageService;
import org.test.editor.util.PathHelper;
import org.test.editor.util.constant.CollaboratorRole;

import java.io.IOException;
import java.util.*;

import static org.test.editor.util.PathHelper.*;

@AllArgsConstructor
@Service
public class ProjectServiceImpl implements ProjectService {

    private final ProjectRepository projectRepository;
    private final FileRepository fileRepository;
    private final LocalFolderStorageService localFolderStorageService;
    private final CollaboratorServiceImpl collaboratorService;
    private final ProjectMapper projectMapper;
    private final FolderRepository folderRepository;
    private final AuthServiceImpl authService;
    private final GitService gitService;
    private final ZipService zipService;

    @Transactional
    public ProjectDTO createProject(CreateProjectDTO createProjectDTO) {
        var currentUserDetails = authService.getCurrentUserDetails();
        var user = currentUserDetails.getUser();
        var project = projectMapper.toEntity(createProjectDTO);
        String projectPath = constructProjectPath(user.getName(), project.getProjectName());
        String archivePath = constructProjectArchivePath(user.getName(), project.getProjectName());
        String versionPath = constructProjectVersionPath(user.getName(), project.getProjectName());
        project.setProjectPath(projectPath);
        project.setVersionPath(versionPath);
        project.setTemplateId(createProjectDTO.templateId());
        var savedProject = projectRepository.save(project);
        var collaborator = new Collaborator().builder()
                .userId(currentUserDetails.getUser().getUserId())
                .projectId(savedProject.getProjectId())
                .collaboratorRoleId(CollaboratorRole.OWNER.getValue())
                .build();
        collaboratorService.createCollaborator(collaborator);
        try {
            localFolderStorageService.createFoldersPath(projectPath);
            localFolderStorageService.createFoldersPath(archivePath);
            localFolderStorageService.createFoldersPath(versionPath);
            return projectMapper.toDTO(project);
        }
        catch (IOException e) {
            rollbackProjectPathsChanges(projectPath, archivePath,versionPath);
            throw new RuntimeException(e.getMessage());
        }
    }


    @Transactional(isolation = Isolation.SERIALIZABLE)
    public ProjectDTO forkProject(ForkProjectDTO forkProjectDTO) {
        var project = projectRepository.findById(forkProjectDTO.projectId())
                .orElseThrow(() -> new ResourceNotFoundException("Project not found"));
        var newProject = createProject(new CreateProjectDTO(forkProjectDTO.projectName(),
                project.getTemplateId(),
                forkProjectDTO.description()));
        var oldFolders = folderRepository.findByProjectId(project.getProjectId());
        var newFolders = new LinkedList<Folder>();
        var map=new HashMap<Integer, Folder>();
        for (var folder : oldFolders) {
            var newFolder= new Folder().builder().projectId(newProject.projectId())
                    .folderName(folder.getFolderName())
                    .folderPath(folder.getFolderPath())
                    .parentFolderId(folder.getParentFolderId())
                    .build();
            newFolders.add(newFolder);
            map.put(folder.getFolderId(), newFolder);
        }
        var savedFolders=folderRepository.saveAll(newFolders);
        var changedFolders=new LinkedList<Folder>();
        for(var folder : savedFolders) {
            if(folder.getParentFolderId()!=null){
                folder.setParentFolderId(map.get(folder.getParentFolderId()).getFolderId());
            }
            folder.setFolderPath(folder.getFolderPath().replace(project.getProjectName(),newProject.projectName()));
            changedFolders.add(folder);

        }

        folderRepository.saveAll(changedFolders);
        var oldFiles = fileRepository.findByFolderIdIn(oldFolders.stream().map(e->e.getFolderId()).toList());
        var newFiles = new LinkedList<File>();
        for (var file : oldFiles) {
            var newFile= new File().builder().
                     folderId(map.get(file.getFolderId()).getFolderId())
                    .fileName(file.getFileName())
                    .filePath(file.getFilePath().replace(project.getProjectName(),newProject.projectName()))
                    .versionPath(file.getVersionPath())
                    .build();
            newFiles.add(newFile);
        }
        fileRepository.saveAll(newFiles);
        try {
            localFolderStorageService.copyInnerFolders(project.getProjectPath(), newProject.projectPath());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return newProject;
    }


    @Transactional
    public void deleteProject(Integer projectId) {
        var currentUserDetails = authService.getCurrentUserDetails();
        var user = currentUserDetails.getUser();
        var project = projectRepository.findById(projectId).orElseThrow();
        collaboratorService.validateCollaborator(user.getName(), projectId);
        String codePath = constructCodePath(user.getName());
        String archivePath = constructProjectArchivePath(user.getName(), project.getProjectName());
        String newArchivePath = generateNewArchivePath(archivePath);
        String newCodePath = getNewCodePath(archivePath, project.getProjectName());
        try {
            moveProjectCodeToArchive(project.getProjectPath(), newCodePath, archivePath, newArchivePath);
            fileRepository.softDeleteByProjectId(project.getProjectId(), codePath, newArchivePath);
            folderRepository.softDeleteByProjectId(project.getProjectId(), codePath, newArchivePath);
            projectRepository.softDeleteByProjectId(projectId, codePath, newArchivePath);
        } catch (IOException e) {
            rollbackProjectCodeToArchive(project.getProjectPath(), newCodePath, archivePath, newArchivePath);
            throw new RuntimeException(e.getMessage());
        }

    }

    public FolderTreeDTO getFolderTree(Integer projectId) {
        var project=projectRepository.findById(projectId).orElseThrow();
        List<Folder> folders = folderRepository.findByProjectId(projectId);
        sortFolders(folders);
        List<File> files = fileRepository.findByFolderIdIn(getFolderIds(folders));
        Map<Integer, List<FileDTO>> folderToFileMap = createFolderToFileMap(files);
        Map<Integer, List<Folder>> folderMap = createFolderMap(folders);
        FolderTreeDTO rootTreeDTO = new FolderTreeDTO();
        rootTreeDTO.setFolderName(project.getProjectName());
        buildFolderTree(rootTreeDTO, folderMap, folderToFileMap, null);
        return rootTreeDTO;
    }
    private void sortFolders(List<Folder> folders) {
        Collections.sort(folders, Comparator.comparing(Folder::getFolderName));
    }
    private Map<Integer, List<Folder>> createFolderMap(List<Folder> folders) {
        Map<Integer, List<Folder>> folderMap = new HashMap<>();

        for (Folder folder : folders) {
            Integer parentId = folder.getParentFolderId();
            folderMap.computeIfAbsent(parentId, k -> new ArrayList<>()).add(folder);
        }

        return folderMap;
    }
    private Map<Integer, List<FileDTO>> createFolderToFileMap(List<File> files) {
        Map<Integer, List<FileDTO>> folderToFileMap = new HashMap<>();

        for (File file : files) {
            Integer folderId = file.getFolderId();
            FileDTO fileDTO = new FileDTO(file.getFileId(), file.getFileName());
            folderToFileMap.computeIfAbsent(folderId, k -> new ArrayList<>()).add(fileDTO);
        }

        return folderToFileMap;
    }
    private List<Integer> getFolderIds(List<Folder> folders) {
        List<Integer> folderIds = new ArrayList<>();
        for (Folder folder : folders) {
            folderIds.add(folder.getFolderId());
        }
        return folderIds;
    }
    private void buildFolderTree(FolderTreeDTO folderTreeDTO, Map<Integer, List<Folder>> folderMap,
                                 Map<Integer, List<FileDTO>> folderToFileMap, Integer parentId) {
        List<FolderTreeDTO> subFolderDTOs = new ArrayList<>();

        List<Folder> subFolders = folderMap.get(parentId);
        if (subFolders != null) {
            for (Folder folder : subFolders) {
                FolderTreeDTO subFolderTree = new FolderTreeDTO();
                subFolderTree.setFolderId(folder.getFolderId());
                subFolderTree.setFolderName(folder.getFolderName());
                List<FileDTO> fileDTOs = folderToFileMap.getOrDefault(folder.getFolderId(), new ArrayList<>());
                subFolderTree.setFiles(fileDTOs);
                buildFolderTree(subFolderTree, folderMap, folderToFileMap, folder.getFolderId());
                subFolderDTOs.add(subFolderTree);
            }
        }

        folderTreeDTO.setSubFolders(subFolderDTOs);
    }
    public List<ProjectWithTemplateDTO> getProjectsByUserId(Integer userId) {
        return projectRepository.findProjectsByUserId(userId);
    }

    public List<ProjectSortedByNameDTO> getAllProjectsSortedByName() {
        return projectRepository.findAllProjectsSortedByName();
    }

    private void moveProjectCodeToArchive(String codePath, String newCodePath, String archivePath, String newArchivePath) throws IOException {
        localFolderStorageService.move(codePath, newCodePath);
        localFolderStorageService.move(archivePath, newArchivePath);
    }

    private void rollbackProjectCodeToArchive(String codePath, String newCodePath, String archivePath, String newArchivePath) {
        try {
            localFolderStorageService.move(newArchivePath, archivePath);
            localFolderStorageService.move(newCodePath, codePath);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void rollbackProjectPathsChanges(String projectPath, String archivePath, String versionPath) {
        try {
            localFolderStorageService.delete(projectPath);
            localFolderStorageService.delete(archivePath);
            localFolderStorageService.delete(versionPath);
        } catch (IOException e) {
            System.err.println("Failed to roll back file changes: " + e.getMessage());
        }
    }

    public java.io.File cloneProject(int projectId) {
        var project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ResourceNotFoundException("Project not found"));
        var projectFullPath = rootLocation.resolve(project.getProjectPath()).normalize().toString();
        try {
            return zipService.zipFolder(projectFullPath);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


}
