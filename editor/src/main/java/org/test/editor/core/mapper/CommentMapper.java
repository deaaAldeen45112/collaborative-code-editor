package org.test.editor.core.mapper;

import org.mapstruct.Mapper;
import org.test.editor.core.dto.CommentDTO;
import org.test.editor.core.dto.CreateCommentDTO;
import org.test.editor.core.dto.UpdateCommentDTO;
import org.test.editor.core.model.Comment;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CommentMapper {
    CommentDTO toDTO(Comment comment);
    Comment toEntity(CreateCommentDTO createCommentDTO);
    Comment toEntity(UpdateCommentDTO updateCommentDTO);
    List<CommentDTO> toDTOList(List<Comment> comments);
}