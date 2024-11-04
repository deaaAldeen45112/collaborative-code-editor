package org.test.editor.core.mapper;

import org.mapstruct.Mapper;
import org.test.editor.core.dto.FileVersionDTO;
import org.test.editor.core.dto.FileVersionWithPathDTO;
import org.test.editor.core.dto.FolderDTO;
import org.test.editor.core.model.Folder;

import java.util.List;

@Mapper(componentModel = "spring")
public interface FileVersionMapper {
    FileVersionDTO toFileVersionDTO(FileVersionWithPathDTO fileVersionWithPathDTOList);
}
