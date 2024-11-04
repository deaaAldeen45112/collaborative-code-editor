package org.test.editor.core.mapper;

import org.mapstruct.Mapper;
import org.test.editor.core.dto.CreateProjectDTO;
import org.test.editor.core.dto.ForkProjectDTO;
import org.test.editor.core.dto.ProjectDTO;
import org.test.editor.core.model.Project;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ProjectMapper {
    ProjectDTO toDTO(Project projectEntity);
    Project toEntity(CreateProjectDTO createProjectDTO);
    CreateProjectDTO toDTO(ForkProjectDTO forkProjectDTO);
    List<ProjectDTO> toDTOList(List<Project> projectEntities);
}
