package org.test.editor.presentation.controller;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.test.editor.core.dto.CommentDTO;
import org.test.editor.core.dto.CreateCommentDTO;
import org.test.editor.core.service.CommentService;
import org.test.editor.util.ApiResponse;

import java.util.List;

@RestController
@RequestMapping("/api/comments")
@RequiredArgsConstructor
@CrossOrigin("*")
public class CommentController {

    private final CommentService commentService;

    @GetMapping("/discussions/{discussionId}")
    public ResponseEntity<ApiResponse<List<CommentDTO>>> getCommentsByDiscussionId(@PathVariable Integer discussionId) {
        List<CommentDTO> commentDTOs = commentService.getCommentsByDiscussionId(discussionId);
        return ResponseEntity.ok(new ApiResponse<>(commentDTOs));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<CommentDTO>> createComment(@RequestBody CreateCommentDTO createCommentDTO) {
        CommentDTO commentDTO = commentService.createComment(createCommentDTO);
        return ResponseEntity.ok(new ApiResponse<>(commentDTO));
    }
}
