package org.test.editor.core.mapper;

import org.mapstruct.Mapper;
import org.test.editor.core.dto.CreateDiscussionDTO;
import org.test.editor.core.dto.DiscussionDTO;
import org.test.editor.core.model.Discussion;

import java.util.List;

@Mapper(componentModel = "spring")
public interface DiscussionMapper {
    DiscussionDTO toDTO(Discussion entity);
    Discussion toEntity(CreateDiscussionDTO createCommentDTO);
    List<DiscussionDTO> toDTOList(List<Discussion> discussions);
}
