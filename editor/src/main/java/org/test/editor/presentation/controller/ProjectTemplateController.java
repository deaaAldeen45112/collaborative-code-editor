package org.test.editor.presentation.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.test.editor.core.dto.ProjectTemplateDTO;
import org.test.editor.core.service.ProjectTemplateService;
import org.test.editor.util.ApiResponse;

import java.util.List;

@RestController
@RequestMapping("/api/project-templates")
@CrossOrigin("*")
public class ProjectTemplateController {
    private final ProjectTemplateService templateService;

    public ProjectTemplateController(ProjectTemplateService templateService) {
        this.templateService = templateService;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<ProjectTemplateDTO>>> getAll() {
        return ResponseEntity.ok(new ApiResponse(templateService.getAll()));
    }
}
