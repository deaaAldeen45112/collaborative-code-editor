package org.test.editor.infra.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.test.editor.core.dto.ProjectTemplateDTO;
import org.test.editor.core.mapper.ProjectTemplateMapper;
import org.test.editor.core.service.ProjectTemplateService;
import org.test.editor.infra.repository.ProjectTemplateRepository;

import java.util.List;

@AllArgsConstructor
@Service
public class ProjectTemplateServiceImpl implements ProjectTemplateService {

    private final ProjectTemplateRepository projectTemplateRepository;
    private final ProjectTemplateMapper projectTemplateMapper;
    public List<ProjectTemplateDTO> getAll(){
        return projectTemplateMapper.toDTO(projectTemplateRepository.findAll());
    }


}
