package org.test.editor.presentation.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.test.editor.core.dto.CreateInvitationDTO;
import org.test.editor.core.dto.InvitationDTO;
import org.test.editor.core.dto.UpdateInvitationStatus;
import org.test.editor.core.model.Invitation;
import org.test.editor.core.service.InvitationService;
import org.test.editor.infra.service.InvitationServiceImpl;
import org.test.editor.util.ApiResponse;

import java.util.List;


@RestController
@RequestMapping("/api/invitations")
@CrossOrigin("*")
public class InvitationController {
    private final InvitationService invitationService;


    public InvitationController(InvitationServiceImpl invitationService) {
        this.invitationService = invitationService;
    }
    @PreAuthorize("hasRole('CODER')")
    @PostMapping
    public ResponseEntity<ApiResponse<Invitation>> createInvitation(@RequestBody CreateInvitationDTO createInvitationDTO) {
        Invitation invitation = invitationService.createInvitation(createInvitationDTO);
        return  ResponseEntity.ok(new ApiResponse(invitation));
    }
    @PreAuthorize("hasRole('CODER')")
    @PatchMapping("/status")
    public ResponseEntity<ApiResponse<Invitation>> updateInvitationStatus(@RequestBody UpdateInvitationStatus updateInvitationStatus) {
        Invitation updatedInvitation = invitationService.updateStatus(updateInvitationStatus);
        return ResponseEntity.ok(new ApiResponse(updatedInvitation));
    }
    @PreAuthorize("hasRole('CODER')")
    @GetMapping("/users/{userId}")
    public ResponseEntity<ApiResponse<List<InvitationDTO>>> getInvitationsByUserId(@PathVariable Integer userId) {
        List<InvitationDTO> invitations = invitationService.getInvitationsByUserId(userId);
        return ResponseEntity.ok(new ApiResponse(invitations));
    }
    @GetMapping("/sent")
    public ResponseEntity<ApiResponse<List<InvitationDTO>>> getSentInvitations(@RequestParam Integer userId) {
        List<InvitationDTO> sentInvitations = invitationService.getSentInvitations(userId);
        return ResponseEntity.ok(new ApiResponse(sentInvitations));
    }


}
