package org.test.editor.core.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProjectSortedByNameDTO {
    private Integer projectId;
    private String projectName;
}
