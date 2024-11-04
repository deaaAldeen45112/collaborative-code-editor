package org.test.editor.core.mapper;

import org.mapstruct.Mapper;
import org.test.editor.core.dto.CreateInvitationDTO;
import org.test.editor.core.model.Invitation;

@Mapper(componentModel = "spring")
public interface InvitationMapper {
    Invitation toEntity(CreateInvitationDTO createInvitationDTO);

}
