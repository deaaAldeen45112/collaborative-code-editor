package org.test.editor.core.dto;

import java.time.LocalDateTime;

public record CommentDTO(Integer commentId,
                         String userName,
                         String content,
                         LocalDateTime createdAt,
                         Integer userId) {
}
