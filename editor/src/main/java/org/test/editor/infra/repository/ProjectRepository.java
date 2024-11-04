package org.test.editor.infra.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.test.editor.core.dto.ProjectSortedByNameDTO;
import org.test.editor.core.dto.ProjectWithTemplateDTO;
import org.test.editor.core.model.Project;

import java.util.List;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Integer> {

     @Query("SELECT new org.test.editor.core.dto.ProjectWithTemplateDTO(p.projectId, p.projectName, pt.templateName,pt.templateId,p.description,p.createdAt) " +
            "FROM Project p " +
            "JOIN Collaborator c ON p.projectId = c.projectId " +
            "LEFT JOIN ProjectTemplate pt ON p.templateId = pt.templateId " +
            "WHERE c.userId = :userId " +
            "ORDER BY p.createdAt DESC")
    List<ProjectWithTemplateDTO> findProjectsByUserId(@Param("userId") Integer userId);

    @Modifying
    @Transactional
    @Query("UPDATE Project f SET f.deletedAt = CURRENT_TIMESTAMP, " +
            "f.projectPath = CONCAT(:newPart, SUBSTRING(f.projectPath, LENGTH(:oldPart) + 1)) " +
            "WHERE f.projectId = :projectId")
    void softDeleteByProjectId(@Param("projectId") Integer projectId,@Param("oldPart") String oldPart, @Param("newPart") String newPart);

    @Query("SELECT new org.test.editor.core.dto.ProjectSortedByNameDTO(p.projectId, p.projectName) " +
            "FROM Project p " +
            "ORDER BY p.projectName ASC")
    List<ProjectSortedByNameDTO> findAllProjectsSortedByName();
}
