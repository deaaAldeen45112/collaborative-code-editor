package org.test.editor.core.service;


import org.test.editor.core.dto.CommentDTO;
import org.test.editor.core.dto.CreateCommentDTO;

import java.util.List;

public interface CommentService {

    List<CommentDTO> getCommentsByDiscussionId(Integer discussionId);
    CommentDTO createComment(CreateCommentDTO createCommentDTO);
}