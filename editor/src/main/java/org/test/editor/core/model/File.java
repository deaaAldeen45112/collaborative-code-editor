package org.test.editor.core.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
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
@Table(name = "files")
@SQLDelete(sql = "UPDATE files SET deleted_at = CURRENT_TIMESTAMP WHERE file_id = ?")
@Where(clause = "deleted_at IS NULL")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class File {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "file_id")
    private Integer fileId;

    @Column(name = "folder_id", nullable = false)
    private Integer folderId;

    @Column(name = "file_name", nullable = false, length = 100)
    private String fileName;

    @Column(name = "file_path", length = 255)
    private String filePath;

    @Column(name = "version_path", length = 200)
    private String versionPath;

    @Column(name = "last_version_number", nullable = false, insertable = false )
    private Integer lastVersionNumber;

    @Column(name = "created_at", insertable = false , updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", insertable = false ,updatable = false)
    private LocalDateTime updatedAt;

    @Column(name = "deleted_at", insertable = false)
    private LocalDateTime deletedAt;

    @OneToMany(mappedBy = "file",fetch = FetchType.LAZY)
    @JsonManagedReference
    @JsonIgnore
    private List<FileVersion> versions;

    @OneToMany(mappedBy = "file",fetch = FetchType.LAZY)
    @JsonManagedReference
    @JsonIgnore
    private List<Discussion> discussions;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "folder_id",insertable = false,updatable = false)
    @JsonBackReference
    @JsonIgnore
    private Folder folder;



}
