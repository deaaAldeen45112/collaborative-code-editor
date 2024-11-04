package org.test.editor.infra.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.test.editor.core.dto.DiscussionDTO;
import org.test.editor.core.model.Discussion;

import java.util.List;

@Repository
public interface DiscussionRepository extends JpaRepository<Discussion, Integer> {


    @Query("SELECT new org.test.editor.core.dto.DiscussionDTO(" +
            "d.discussionId, d.topic, d.startLineNum, d.fileId, " +
            "f.fileName, " +
            "CASE " +
            "    WHEN INSTR(f.filePath, '/projects/code') > 0 THEN " +
            "        SUBSTRING(f.filePath, INSTR(f.filePath, '/projects/code') + LENGTH('/projects/code')) " +
            "    ELSE f.filePath " +
            "END, " +
            "d.createdAt) " +
            "FROM Discussion d " +
            "JOIN File f ON f.fileId = d.fileId " +
            "WHERE f.folder.projectId = :projectId " +
            "ORDER BY d.createdAt DESC")
    List<DiscussionDTO> findDiscussionDTOByProjectIdOrderByCreatedAtDesc(@Param("projectId") Integer projectId);

}
