package org.test.editor.core.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "collaborators")
@SQLDelete(sql = "UPDATE collaborator SET deleted_at = CURRENT_TIMESTAMP WHERE collaborator_id = ?")
@Where(clause = "deleted_at IS NULL")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Collaborator {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "collaborator_id")
    private Integer collaboratorId;

    @Column(name = "project_id")
    private Integer projectId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id",insertable = false,updatable = false)
    @JsonBackReference
    @JsonIgnore
    private Project project;

    @Column(name = "user_id")
    private Integer userId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id",insertable = false,updatable = false)
    @JsonBackReference
    @JsonIgnore
    private User user;

    @Column(name = "collaborator_role_id")
    private Integer collaboratorRoleId;

    @ManyToOne
    @JoinColumn(name = "collaborator_role_id",insertable = false,updatable = false)
    @JsonBackReference
    @JsonIgnore
    private CollaboratorRole role;

    @Column(name = "created_at",insertable = false,updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at",insertable = false,updatable = false)
    private LocalDateTime updatedAt;

    @Column(name = "deleted_at",insertable = false)
    private LocalDateTime deletedAt;

}