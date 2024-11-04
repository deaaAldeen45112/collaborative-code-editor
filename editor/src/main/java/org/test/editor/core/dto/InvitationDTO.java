package org.test.editor.core.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class InvitationDTO {
    private Integer invitationId;
    private String userName;
    private String projectName;
    private String statusName;
    private LocalDateTime invitationSentAt;;
    private LocalDateTime expiresAt;
    private Integer statusId;
    private LocalDateTime acceptedAt;
}