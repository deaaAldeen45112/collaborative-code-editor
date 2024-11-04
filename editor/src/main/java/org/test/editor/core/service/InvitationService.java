package org.test.editor.core.service;

import org.test.editor.core.dto.CreateInvitationDTO;
import org.test.editor.core.dto.InvitationDTO;
import org.test.editor.core.dto.UpdateInvitationStatus;
import org.test.editor.core.model.Invitation;

import java.util.List;

public interface InvitationService {
    Invitation createInvitation(CreateInvitationDTO createInvitationDTO);
    Invitation updateStatus(UpdateInvitationStatus updateInvitationStatus);
    List<InvitationDTO> getInvitationsByUserId(Integer userId);
    List<InvitationDTO> getSentInvitations(Integer userId);
}