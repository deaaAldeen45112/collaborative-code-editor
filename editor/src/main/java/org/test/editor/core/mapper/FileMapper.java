package org.test.editor.core.mapper;

import org.mapstruct.Mapper;
import org.test.editor.core.dto.CreateFileDTO;
import org.test.editor.core.dto.FileDTO;
import org.test.editor.core.dto.UpdateFileDTO;
import org.test.editor.core.model.File;

import java.util.List;
@Mapper(componentModel = "spring")
public interface FileMapper {
    FileDTO toDTO(File fileEntity);
    File toEntity(CreateFileDTO createFileDTO);
    File toEntity(UpdateFileDTO updateFileDTO);
    List<FileDTO> toDTOList(List<File> fileEntities);
}