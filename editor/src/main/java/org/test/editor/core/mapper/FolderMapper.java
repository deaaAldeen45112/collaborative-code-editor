package org.test.editor.core.mapper;
import org.mapstruct.Mapper;
import org.test.editor.core.dto.CreateFolderDTO;
import org.test.editor.core.dto.CreatePrimaryFolderDTO;
import org.test.editor.core.dto.CreateRootFolderDTO;
import org.test.editor.core.dto.FolderDTO;
import org.test.editor.core.model.Folder;

import java.util.List;

@Mapper(componentModel = "spring")
public interface FolderMapper {
    Folder toEntity(CreateRootFolderDTO createRootFolderDTO);
    Folder toEntity(CreatePrimaryFolderDTO createPrimaryFolderDTO);
    FolderDTO toDTO(Folder folderEntity);
    Folder toEntity(CreateFolderDTO createFolderDTO);
    List<FolderDTO> toDTOList(List<Folder> folderEntities);
}
