package org.test.editor.core.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Entity
@Table(name = "invitation_statuses")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InvitationStatus {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "status_id")
    private Integer statusId;

    @Column(name = "status_name")
    private String statusName;

    @Column(name = "created_at",insertable = false,updatable = false)
    private LocalDateTime createdAt;

    @OneToMany(mappedBy = "status")
    @JsonManagedReference
    @JsonIgnore
    private List<Invitation> invitations;

}

