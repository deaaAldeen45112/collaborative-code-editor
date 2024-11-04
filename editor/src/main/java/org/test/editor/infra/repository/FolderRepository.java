package org.test.editor.infra.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.test.editor.core.model.Folder;

import java.util.List;

@Repository
public interface FolderRepository extends JpaRepository<Folder, Integer> {

    @Modifying
    @Transactional
    @Query(value = "UPDATE folders f " +
            "SET f.deleted_at = CURRENT_TIMESTAMP, " +
            "f.folder_path = CONCAT(:newPart, SUBSTRING(f.folder_path, LENGTH(:oldPart) + 1)) " +
            "WHERE f.folder_id IN ( " +
            "WITH RECURSIVE folder_tree AS ( " +
            "SELECT f.folder_id " +
            "FROM folders f " +
            "WHERE f.folder_id = :folderId " +
            "UNION ALL " +
            "SELECT child.folder_id " +
            "FROM folders child " +
            "INNER JOIN folder_tree parent ON child.parent_folder_id = parent.folder_id " +
            ") " +
            "SELECT folder_id FROM folder_tree)",
            nativeQuery = true)
    void softDeleteByFolderId(@Param("folderId") Integer folderId,@Param("oldPart") String oldPart, @Param("newPart") String newPart);

    @Modifying
    @Transactional
    @Query("UPDATE Folder f SET f.deletedAt = CURRENT_TIMESTAMP, " +
            "f.folderPath = CONCAT(:newPart, SUBSTRING(f.folderPath, LENGTH(:oldPart) + 1)) " +
            "WHERE f.projectId = :projectId")
    void softDeleteByProjectId(@Param("projectId") Integer projectId,@Param("oldPart") String oldPart, @Param("newPart") String newPart);

    List<Folder> findByProjectId(Integer projectId);
}
