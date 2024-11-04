package org.test.editor.core.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class FolderTreeDTO {
    private Integer folderId;
    private String folderName;
    private List<FolderTreeDTO> subFolders;
    private List<FileDTO> files;

}
