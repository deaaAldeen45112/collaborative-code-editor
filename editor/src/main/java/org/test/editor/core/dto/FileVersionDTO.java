package org.test.editor.core.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FileVersionDTO {
    private Integer versionId;
    private Integer versionNumber;
    private String diffContent;
    private Integer logId;
    private String userName;
    private LocalDateTime createdAt;

}
