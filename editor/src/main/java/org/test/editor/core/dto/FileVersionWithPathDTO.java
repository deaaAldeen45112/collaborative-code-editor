package org.test.editor.core.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class FileVersionWithPathDTO {
    private Integer versionId;
    private Integer versionNumber;
    private String diffContent;
    private String path;
    private Integer logId;
    private String userName;
    private LocalDateTime createdAt;
}
