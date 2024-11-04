package org.test.editor.core.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import jakarta.persistence.*;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Entity
@Table(name = "folders")
@SQLDelete(sql = "UPDATE folders SET deleted_at = CURRENT_TIMESTAMP WHERE folder_id = ?")
@Where(clause = "deleted_at IS NULL")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Folder {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "folder_id")
    private Integer folderId;

    @Column(name = "project_id", nullable = false)
    private Integer projectId;

    @Column(name = "folder_path", nullable = false, length = 100)
    private String folderPath;

    @Column(name = "folder_name", nullable = false, length = 100)
    private String folderName;

    @Column(name = "parent_folder_id")
    private Integer parentFolderId;

    @Column(name = "created_at", insertable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at",  insertable = false,updatable = false)
    private LocalDateTime updatedAt;

    @Column(name = "deleted_at",insertable = false)
    private LocalDateTime deletedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id",insertable = false,updatable = false)
    @JsonBackReference
    @JsonIgnore
    private Project project;



//    @OneToMany(mappedBy = "parentFolder",fetch = FetchType.LAZY)
//    @JsonManagedReference
//    @JsonIgnore
//    private List<Folder> subFolders;

    @OneToMany(mappedBy = "folder",fetch = FetchType.LAZY)
    @JsonManagedReference
    @JsonIgnore
    private List<File> files;
}
