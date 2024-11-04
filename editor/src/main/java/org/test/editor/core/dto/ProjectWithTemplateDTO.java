package org.test.editor.core.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProjectWithTemplateDTO {
    private Integer projectId;
    private String projectName;
    private String templateName;
    private Integer templateId;
    private String description;
    private LocalDateTime createdAt;
}
