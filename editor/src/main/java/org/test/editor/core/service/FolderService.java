package org.test.editor.core.service;

import org.test.editor.core.dto.CreateFolderDTO;
import org.test.editor.core.dto.CreatePrimaryFolderDTO;
import org.test.editor.core.dto.FolderDTO;
import org.test.editor.core.model.Folder;

import java.util.Optional;

public interface FolderService {
    FolderDTO createPrimaryFolder(CreatePrimaryFolderDTO createPrimaryFolderDTO);
    FolderDTO createFolder(CreateFolderDTO createFolderDTO);
    Optional<Folder> getFolderById(Integer folderId);
    void delete(Integer folderId);
}