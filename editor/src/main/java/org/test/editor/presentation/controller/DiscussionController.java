package org.test.editor.presentation.controller;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.test.editor.core.dto.CreateDiscussionDTO;
import org.test.editor.core.dto.DiscussionDTO;
import org.test.editor.core.service.DiscussionService;
import org.test.editor.util.ApiResponse;

import java.util.List;

@RestController
@RequestMapping("/api/discussions")
@RequiredArgsConstructor
@CrossOrigin("*")
public class DiscussionController {

    private final DiscussionService discussionService;

    @GetMapping("/projects/{projectId}")
    public ResponseEntity<ApiResponse<List<DiscussionDTO>>> getDiscussionsByProjectId(@PathVariable Integer projectId) {
        List<DiscussionDTO> discussionDTOs = discussionService.getDiscussionsByProjectId(projectId);
        return ResponseEntity.ok(new ApiResponse(discussionDTOs));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<DiscussionDTO>> createDiscussion(@RequestBody CreateDiscussionDTO createDiscussionDTO) {
        DiscussionDTO discussionDTO = discussionService.createDiscussion(createDiscussionDTO);
        return ResponseEntity.ok(new ApiResponse(discussionDTO));
    }
}
