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
@Table(name = "version_logs")

@Builder
@AllArgsConstructor
@NoArgsConstructor
public class VersionLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "log_id")
    private Integer logId;

    @Column(name = "action", nullable = false)
    private String action;

    @Column(name = "version_id")
    private Integer versionId;

    @Column(name = "user_id")
    private Integer userId;

    @Column(name = "created_at",insertable = false, updatable = false)
    private LocalDateTime createdAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "version_id",referencedColumnName = "version_id",insertable=false,updatable=false)
    @JsonBackReference
    @JsonIgnore
    private FileVersion version;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id",referencedColumnName = "user_id",insertable=false,updatable=false)
    @JsonBackReference
    @JsonIgnore
    private User user;


    public VersionLog(Integer versionId, Integer userId, String action) {
        this.action = action;
        this.versionId = versionId;
        this.userId = userId;
    }

}
