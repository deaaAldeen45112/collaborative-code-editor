package org.test.editor.core.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

public record DiscussionDTO (Integer discussionId,
                             String topic,
                             Integer startLineNum,
                             Integer fileId,
                             String fileName,
                             String filePath,
                             LocalDateTime createdAt){
}
