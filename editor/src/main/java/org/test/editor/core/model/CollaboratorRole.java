package org.test.editor.core.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Entity
@Table(name = "collaborator_roles")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CollaboratorRole {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "collaborator_role_id")
    private Integer collaboratorRoleId;

    @Column(name = "role_name")
    private String roleName;

    @OneToMany(mappedBy = "role")
    @JsonManagedReference
    @JsonIgnore
    private List<Collaborator> collaborators;

}

