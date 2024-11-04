package org.test.editor.infra.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.test.editor.core.exception.ResourceNotFoundException;
import org.test.editor.core.model.Collaborator;
import org.test.editor.core.service.AuthService;
import org.test.editor.core.service.CollaboratorService;
import org.test.editor.infra.repository.CollaboratorRepository;
import org.test.editor.infra.repository.FileRepository;
import org.test.editor.infra.repository.FolderRepository;
import org.test.editor.util.constant.CollaboratorRole;

@Service
@RequiredArgsConstructor
public class CollaboratorServiceImpl implements CollaboratorService {

    private final CollaboratorRepository collaboratorRepository;
    private final AuthService authService;
    private final FolderRepository folderRepository;
    private final FileRepository fileRepository;

    public void validateCollaborator(String username, Integer projectId) {
        if (!collaboratorRepository.existsByUserNameAndProjectId(username, projectId)) {
            throw new AccessDeniedException("User does not have access to this project");
        }
    }
    public void validateUserProjectOwner(Integer userId, Integer projectId) {
        if (!collaboratorRepository.isUserProjectOwner(userId, projectId, CollaboratorRole.OWNER.getValue())) {
            throw new AccessDeniedException("User are not owner to this project");
        }
    }

    public void validateCollaboratorByProjectId(Integer projectId) {
        var username=authService.getCurrentUserDetails().getUser().getName();
        validateCollaborator(username, projectId);
    }
    public void validateCollaboratorByFileId(Integer fileId) {
        var user=authService.getCurrentUserDetails().getUser();
        var file=fileRepository.findById(fileId)
                .orElseThrow(() -> new ResourceNotFoundException("File not found"));
        var folder=folderRepository.findById(file.getFolderId())
                .orElseThrow(() -> new ResourceNotFoundException("File not found"));
        validateCollaborator(user.getName(),folder.getProjectId());

    }
    public void validateCollaboratorByFolderId(Integer folderId) {
         var user=authService.getCurrentUserDetails().getUser();
         var folder=folderRepository.findById(folderId)
                .orElseThrow(() -> new ResourceNotFoundException("File not found"));
        validateCollaborator(user.getName(),folder.getProjectId());
    }
    public Collaborator createCollaborator(Collaborator collaborator) {
        return collaboratorRepository.save(collaborator);
    }
}