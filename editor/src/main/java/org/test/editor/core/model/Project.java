package org.test.editor.core.model;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Entity
@Table(name = "projects")
@SQLDelete(sql = "UPDATE projects SET deleted_at = CURRENT_TIMESTAMP WHERE project_id = ?")
@Where(clause = "deleted_at IS NULL")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Project {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "project_id")
    private Integer projectId;

    @Column(name = "project_name", nullable = false, length = 100)
    private String projectName;

    @Column(name = "project_path", length = 200)
    private String projectPath;

    @Column(name = "version_path", length = 200)
    private String versionPath;

    @Column(name = "description", length = 200)
    private String description;

    @Column(name = "template_id")
    private Integer templateId;

    @Column(name = "created_at", insertable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", insertable = false, updatable = false)
    private LocalDateTime updatedAt;

    @Column(name = "deleted_at",insertable = false)
    private LocalDateTime deletedAt;

    @OneToMany(mappedBy = "project",fetch = FetchType.LAZY)
    @JsonManagedReference
    @JsonIgnore
    private List<Collaborator> collaborators;

    @OneToMany(mappedBy = "project",fetch = FetchType.LAZY)
    @JsonManagedReference
    @JsonIgnore
    private List<Folder> folders;

    @OneToMany(mappedBy = "project",fetch = FetchType.LAZY)
    @JsonManagedReference
    @JsonIgnore
    private List<Invitation> invitations;
}
