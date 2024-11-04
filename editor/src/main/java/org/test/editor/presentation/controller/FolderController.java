package org.test.editor.presentation.controller;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.test.editor.core.dto.CreateFolderDTO;
import org.test.editor.core.dto.CreatePrimaryFolderDTO;
import org.test.editor.core.dto.FolderDTO;
import org.test.editor.core.model.Folder;
import org.test.editor.core.service.CollaboratorService;
import org.test.editor.core.service.FolderService;

import java.util.Optional;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/folders")
@CrossOrigin("*")
public class FolderController {

    private final FolderService folderService;
    private final CollaboratorService collaboratorService;

    @PreAuthorize("hasRole('CODER')")
    @PostMapping
    public ResponseEntity<FolderDTO> createFolder(@RequestBody CreateFolderDTO folder) {
        collaboratorService.validateCollaboratorByFolderId(folder.parentFolderId());
        FolderDTO createdFolder = folderService.createFolder(folder);
        return ResponseEntity.ok(createdFolder);
    }
    @PreAuthorize("hasRole('CODER')")
    @PostMapping("/primary")
    public ResponseEntity<FolderDTO> createPrimaryFolder(@RequestBody CreatePrimaryFolderDTO createPrimaryFolderDTO) {
        collaboratorService.validateCollaboratorByProjectId(createPrimaryFolderDTO.projectId());
        FolderDTO createdFolder = folderService.createPrimaryFolder(createPrimaryFolderDTO);
        return ResponseEntity.ok(createdFolder);
    }
    @PreAuthorize("hasRole('CODER')")
    @GetMapping("/{id}")
    public ResponseEntity<Optional<Folder>> getFolderById(@PathVariable Integer id) {
        collaboratorService.validateCollaboratorByFileId(id);
        return ResponseEntity.ok(folderService.getFolderById(id));
    }
    @PreAuthorize("hasRole('CODER')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFolder(@PathVariable Integer id) {
        collaboratorService.validateCollaboratorByFolderId(id);
        folderService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
