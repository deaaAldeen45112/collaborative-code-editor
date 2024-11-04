package org.test.editor.infra.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.test.editor.core.model.ProjectTemplate;


@Repository
public interface ProjectTemplateRepository extends JpaRepository<ProjectTemplate, Integer> {



}
