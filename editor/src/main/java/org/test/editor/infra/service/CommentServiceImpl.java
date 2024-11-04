package org.test.editor.infra.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.test.editor.core.dto.CommentDTO;
import org.test.editor.core.dto.CreateCommentDTO;
import org.test.editor.core.exception.ResourceNotFoundException;
import org.test.editor.core.mapper.CommentMapper;
import org.test.editor.core.service.CommentService;
import org.test.editor.infra.repository.CommentRepository;


import java.util.List;

@RequiredArgsConstructor
@Service
public class CommentServiceImpl implements CommentService {
    private final CommentRepository commentRepository;
    private final CommentMapper commentMapper;

    public List<CommentDTO> getCommentsByDiscussionId(Integer discussionId) {
        return commentRepository.findCommentsByDiscussionId(discussionId);
    }


    public CommentDTO createComment(CreateCommentDTO createCommentDTO) {
            var comment = commentMapper.toEntity(createCommentDTO);
            var savedComment=commentRepository.save(comment);
            return commentRepository.findByCommentId(savedComment.getCommentId())
                    .orElseThrow(() -> new ResourceNotFoundException("comment not found"));
    }

}
