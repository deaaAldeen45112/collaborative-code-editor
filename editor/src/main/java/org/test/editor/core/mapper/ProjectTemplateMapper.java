package org.test.editor.core.mapper;

import org.mapstruct.Mapper;
import org.test.editor.core.dto.ProjectTemplateDTO;
import org.test.editor.core.model.ProjectTemplate;

import java.util.List;
@Mapper(componentModel = "spring")
public interface ProjectTemplateMapper {
    ProjectTemplateDTO toDTO(ProjectTemplate projectTemplate);
    List<ProjectTemplateDTO> toDTO(List<ProjectTemplate> projectTemplates);

}
