package org.test.editor.core.service;

import org.test.editor.core.model.Collaborator;

public interface CollaboratorService {

    void validateCollaborator(String username, Integer projectId);
    void validateUserProjectOwner(Integer userId, Integer projectId);
    void validateCollaboratorByProjectId(Integer projectId);
    void validateCollaboratorByFileId(Integer fileId);
    void validateCollaboratorByFolderId(Integer folderId);
    Collaborator createCollaborator(Collaborator collaborator);
}