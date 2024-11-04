package org.test.editor.presentation.controller;

import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.test.editor.core.dto.*;
import org.test.editor.core.service.CollaboratorService;
import org.test.editor.core.service.ProjectService;
import org.test.editor.infra.service.CollaboratorServiceImpl;
import org.test.editor.util.ApiResponse;

import java.io.File;
import java.io.FileInputStream;
import java.util.List;

@RestController
@RequestMapping("/api/projects")
@CrossOrigin("*")
public class ProjectController {
    private final ProjectService projectService;
    private final CollaboratorService collaboratorService;

    @Autowired
    public ProjectController(ProjectService projectService, CollaboratorServiceImpl collaboratorService) {
        this.projectService = projectService;
        this.collaboratorService = collaboratorService;
    }

    @PreAuthorize("hasRole('CODER')")
    @PostMapping()
    public ResponseEntity<ApiResponse<ProjectDTO>> createProject(@Valid @RequestBody CreateProjectDTO createProjectDTO) {
        ProjectDTO createdProject = projectService.createProject(createProjectDTO);
        ApiResponse response = new ApiResponse("project created successfully", "success", createdProject);
        return ResponseEntity.ok(response);
    }


    @PreAuthorize("hasRole('CODER')")
    @PostMapping("fork")
    public ResponseEntity<ApiResponse<ProjectDTO>> forkProject(@Valid @RequestBody ForkProjectDTO forkProjectDTO) {
        collaboratorService.validateCollaboratorByProjectId(forkProjectDTO.projectId());
        ProjectDTO createdProject = projectService.forkProject(forkProjectDTO);
        ApiResponse response = new ApiResponse("project forked successfully", "success", createdProject);
        return ResponseEntity.ok(response);
    }


    @PreAuthorize("hasRole('CODER')")
    @GetMapping("project-tree/{id}")
    public ResponseEntity<ApiResponse<FolderTreeDTO>> getFolderTree(@PathVariable Integer id) {
        collaboratorService.validateCollaboratorByProjectId(id);
        var folderTree = projectService.getFolderTree(id);
        ApiResponse response = new ApiResponse(folderTree);
        return ResponseEntity.ok(response);
    }


    @PreAuthorize("hasRole('CODER')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProject(@PathVariable Integer id) {
        projectService.deleteProject(id);
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasRole('CODER')")
    @GetMapping("users/{userId}")
    public ResponseEntity<ApiResponse<List<ProjectWithTemplateDTO>>> getProjectsByUserId(@PathVariable Integer userId) {
        List<ProjectWithTemplateDTO> projects = projectService.getProjectsByUserId(userId);
        return ResponseEntity.ok(new ApiResponse<>(projects));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/name-sorted")
    public ResponseEntity<List<ProjectSortedByNameDTO>> getAllProjectsSortedByName() {
        List<ProjectSortedByNameDTO> projects = projectService.getAllProjectsSortedByName();
        return ResponseEntity.ok(projects);
    }

    @PreAuthorize("hasRole('CODER')")
    @GetMapping("/clone/{projectId}")
    public ResponseEntity<InputStreamResource> cloneProject(@PathVariable int projectId) {
        try {
            collaboratorService.validateCollaboratorByProjectId(projectId);
            var zipFile = projectService.cloneProject(projectId);
            InputStreamResource resource = new InputStreamResource(new FileInputStream(zipFile));
            HttpHeaders headers = new HttpHeaders();
            headers.add("Content-Disposition", "attachment; filename=" + zipFile.getName());

            return ResponseEntity.ok()
                    .headers(headers)
                    .contentLength(zipFile.length())
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .body(resource);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
}
