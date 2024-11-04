package org.test.editor.infra.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.test.editor.core.dto.FileVersionDTO;
import org.test.editor.core.dto.FileVersionWithPathDTO;
import org.test.editor.core.model.FileVersion;

import java.util.List;

@Repository
public interface FileVersionRepository extends JpaRepository<FileVersion,Integer> {

    @Query("SELECT new org.test.editor.core.dto.FileVersionWithPathDTO(fv.versionId, fv.versionNumber, fv.diffContent, fv.path, vl.logId, u.name,fv.createdAt) " +
            "FROM FileVersion fv " +
            "LEFT JOIN VersionLog vl ON fv.versionId = vl.versionId " +
            "LEFT JOIN User u ON vl.userId = u.userId " +
            "WHERE fv.fileId = :fileId " +
            "ORDER BY fv.versionNumber DESC")
    List<FileVersionWithPathDTO> findByFileIdSorted(Integer fileId, Pageable pageable);
}
