package org.test.editor.core.service;

import org.test.editor.core.dto.CreateProjectDTO;
import org.test.editor.core.dto.FolderTreeDTO;
import org.test.editor.core.dto.ProjectDTO;
import org.test.editor.core.dto.ProjectSortedByNameDTO;
import org.test.editor.core.dto.ProjectWithTemplateDTO;
import org.test.editor.core.dto.ForkProjectDTO;

import java.io.File;
import java.util.List;

public interface ProjectService {
    ProjectDTO createProject(CreateProjectDTO createProjectDTO);
    ProjectDTO forkProject(ForkProjectDTO forkProjectDTO);
    void deleteProject(Integer projectId);
    FolderTreeDTO getFolderTree(Integer projectId);
    List<ProjectWithTemplateDTO> getProjectsByUserId(Integer userId);
    List<ProjectSortedByNameDTO> getAllProjectsSortedByName();
    File cloneProject(int projectId);
}