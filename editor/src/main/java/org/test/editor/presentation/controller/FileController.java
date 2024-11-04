package org.test.editor.presentation.controller;


import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.test.editor.core.dto.CreateFileDTO;
import org.test.editor.core.dto.FileVersionDTO;
import org.test.editor.core.dto.UpdateFileDTO;
import org.test.editor.core.model.File;
import org.test.editor.core.service.AuthService;
import org.test.editor.core.service.CollaboratorService;
import org.test.editor.core.service.FileService;
import org.test.editor.core.service.FileVersionService;
import org.test.editor.infra.service.AuthServiceImpl;
import org.test.editor.infra.service.CollaboratorServiceImpl;
import org.test.editor.util.ApiResponse;

import java.io.IOException;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/files")
@CrossOrigin("*")
public class FileController {

    private final FileService fileService;
    private final FileVersionService fileVersionService;
    private final CollaboratorService collaboratorService;
    private final AuthService authService;

    @PreAuthorize("hasRole('CODER')")
    @PostMapping("/create")
    public ResponseEntity<File> createFile(@RequestBody CreateFileDTO file) {
        var user=authService.getCurrentUserDetails().getUser();
        var createdFile = fileService.createFileByUser(file,user.getUserId());
        return ResponseEntity.ok(createdFile);
    }

    @PreAuthorize("hasRole('CODER')")
    @PostMapping("/update")
    public ResponseEntity<File> editFile(@RequestBody UpdateFileDTO file) throws IOException {
        var user=authService.getCurrentUserDetails().getUser();
        fileService.editFileByUser(file,user.getUserId());
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasRole('CODER')")
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<String>> getFileById(@PathVariable Integer id) {
        collaboratorService.validateCollaboratorByFileId(id);
        return ResponseEntity.ok(new ApiResponse(fileService.getFileById(id)));
    }

    @PreAuthorize("hasRole('CODER')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFile(@PathVariable Integer id) {
        fileService.deleteFile(id);
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasRole('CODER')")
    @GetMapping("/{fileId}/versions")
    public ResponseEntity<ApiResponse<List<FileVersionDTO>>> getFileVersions(
            @PathVariable Integer fileId,
            @RequestParam(defaultValue = "100") int limit) {
        collaboratorService.validateCollaboratorByFileId(fileId);
        List<FileVersionDTO> fileVersions = fileVersionService.getFileVersionsByFileId(fileId, limit);
        return ResponseEntity.ok(new ApiResponse(fileVersions));
    }

}