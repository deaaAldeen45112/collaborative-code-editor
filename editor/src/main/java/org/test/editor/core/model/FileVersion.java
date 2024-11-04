package org.test.editor.core.model;


import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Entity
@Table(name = "file_versions")
@Builder
@AllArgsConstructor
public class FileVersion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "version_id")
    private Integer versionId;

    @Column(name = "file_id", nullable = false)
    private Integer fileId;

    @Column(name = "version_number", nullable = false)
    private Integer versionNumber;

    @Column(name = "diff_content",length = 2000)
    private String diffContent;

    @Column(name = "path")
    private String path;


    @Column(name = "created_at", insertable = false,updatable = false)
    private LocalDateTime createdAt;

    @ManyToOne()
    @JoinColumn(name = "file_id",insertable = false,updatable = false)
    @JsonBackReference
    @JsonIgnore
    private File file;

    @OneToMany(mappedBy = "version",fetch = FetchType.LAZY)
    @JsonManagedReference
    @JsonIgnore
    private List<VersionLog> versionLogs;

    public FileVersion(Integer fileId, Integer versionNumber, String diffContent) {
        this.fileId = fileId;
        this.versionNumber = versionNumber;
        this.diffContent = diffContent;
    }
    public FileVersion(Integer fileId, Integer versionNumber) {
        this.fileId = fileId;
        this.versionNumber = versionNumber;
        this.diffContent = diffContent;
    }

    public FileVersion() {}
}

