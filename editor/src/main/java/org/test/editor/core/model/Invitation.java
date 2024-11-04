package org.test.editor.core.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "invitations")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Invitation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "invitation_id")
    private Integer invitationId;

    @Column(name = "project_id")
    private Integer projectId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id", referencedColumnName = "project_id", insertable = false, updatable = false)
    @JsonBackReference
    @JsonIgnore
    private Project project;

    @Column(name = "user_id")
    private Integer userId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", referencedColumnName = "user_id", insertable = false, updatable = false)
    @JsonBackReference
    @JsonIgnore
    private User user;

    @Column(name = "invitation_sent_at",insertable = false,updatable = false)
    private LocalDateTime invitationSentAt;

    @Column(name = "accepted_at",insertable = false)
    private LocalDateTime acceptedAt;

    @Column(name = "expires_at",updatable = false)
    private LocalDateTime expiresAt;

    @Column(name = "status_id")
    private Integer statusId;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "status_id",referencedColumnName = "status_id",updatable = false,insertable = false)
    @JsonBackReference
    @JsonIgnore
    private InvitationStatus status;

}

