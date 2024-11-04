package org.test.editor.infra.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.server.ResponseStatusException;
import org.test.editor.core.dto.CreateInvitationDTO;
import org.test.editor.core.dto.InvitationDTO;
import org.test.editor.core.dto.UpdateInvitationStatus;
import org.test.editor.core.mapper.InvitationMapper;
import org.test.editor.core.model.Collaborator;
import org.test.editor.core.model.Invitation;
import org.test.editor.core.service.InvitationService;
import org.test.editor.infra.repository.InvitationRepository;
import org.test.editor.util.constant.CollaboratorRole;
import org.test.editor.util.constant.InvitationStatus;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

@RequiredArgsConstructor
@Service
public class InvitationServiceImpl implements InvitationService {
    private final InvitationRepository invitationRepository;
    private final InvitationMapper invitationMapper;
    private final CollaboratorServiceImpl collaboratorService;
    private final AuthServiceImpl authService;

    public Invitation createInvitation(CreateInvitationDTO createInvitationDTO) {
        var user = authService.getCurrentUserDetails().getUser();
        collaboratorService.validateUserProjectOwner(user.getUserId(), createInvitationDTO.projectId());
        var invitation = invitationMapper.toEntity(createInvitationDTO);
        invitation.setExpiresAt(LocalDateTime.now().plus(7, ChronoUnit.DAYS));
        invitation.setStatusId(InvitationStatus.PENDING.getValue());
        return invitationRepository.save(invitation);
    }

    public Invitation updateStatus(UpdateInvitationStatus updateInvitationStatus) {
        var invitation = invitationRepository.findById(updateInvitationStatus.invitationId())
                .orElseThrow(() -> new HttpClientErrorException(HttpStatus.NOT_FOUND, "Invitation not found"));

        validateInvitationNotExpired(invitation);
        validateInvitationStatusIsPending(invitation);

        updateInvitationStatus(invitation, updateInvitationStatus);

        return invitationRepository.save(invitation);
    }

    private void validateInvitationNotExpired(Invitation invitation) {
        if (LocalDateTime.now().isAfter(invitation.getExpiresAt())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The invitation has expired.");
        }
    }

    private void validateInvitationStatusIsPending(Invitation invitation) {
        if (invitation.getStatusId() != InvitationStatus.PENDING.getValue()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "The invitation status is not PENDING.");
        }
    }

    private void updateInvitationStatus(Invitation invitation, UpdateInvitationStatus updateInvitationStatus) {
        if (updateInvitationStatus.invitationStatusId() == InvitationStatus.ACCEPTED.getValue()) {
            acceptInvitation(invitation);
        } else if (updateInvitationStatus.invitationStatusId() == InvitationStatus.REJECTED.getValue()) {
            rejectInvitation(invitation);
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid status update.");
        }
    }

    private void acceptInvitation(Invitation invitation) {
        invitation.setAcceptedAt(LocalDateTime.now());
        invitation.setStatusId(InvitationStatus.ACCEPTED.getValue());

        Collaborator collaborator = Collaborator.builder()
                .userId(invitation.getUserId())
                .projectId(invitation.getProjectId())
                .collaboratorRoleId(CollaboratorRole.MEMBER.getValue())
                .build();

        collaboratorService.createCollaborator(collaborator);
    }

    private void rejectInvitation(Invitation invitation) {
        invitation.setStatusId(InvitationStatus.REJECTED.getValue());
    }

    public List<InvitationDTO> getInvitationsByUserId(Integer userId) {
        return invitationRepository.findAllInvitationsByUserId(userId);
    }

    public List<InvitationDTO> getSentInvitations(Integer userId) {
        return invitationRepository.findSentInvitations(userId);
    }
}
