package org.test.editor.core.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@Entity
@Table(name = "discussion")
@NoArgsConstructor
@AllArgsConstructor
public class Discussion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "discussion_id")
    private Integer discussionId;

    @Column(name = "file_id")
    private Integer fileId;

    @Column(name = "start_line_num")
    private Integer startLineNum;

    @Column(name = "topic")
    private String topic;

    @Column(name = "created_at",insertable = false,updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at",insertable = false,updatable = false)
    private LocalDateTime updatedAt;

    @Column(name = "deleted_at",insertable = false)
    private LocalDateTime deletedAt;

    @OneToMany(mappedBy = "discussion",fetch = FetchType.LAZY)
    private List<Comment> comments;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "file_id",referencedColumnName = "file_id", insertable = false, updatable = false)
    @JsonBackReference
    @JsonIgnore
    private File file;


}
