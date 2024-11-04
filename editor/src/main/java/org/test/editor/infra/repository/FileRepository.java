package org.test.editor.infra.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.test.editor.core.model.File;

import java.util.List;
import java.util.Optional;

@Repository
public interface FileRepository extends JpaRepository<File, Integer> {
   Optional<File> findByFileName(String fileName);

   @Query("SELECT f FROM File f WHERE f.fileId = :fileId")
   Optional<File> findIncludingDeleted(@Param("fileId") Integer fileId);

   @Query("SELECT f FROM File f WHERE f.fileId = :fileId")
   Optional<File> findFileById(@Param("fileId") Integer fileId);

   @Modifying
   @Transactional
   @Query(value = "UPDATE files f " +
           "SET f.deleted_at = CURRENT_TIMESTAMP, " +
           "f.file_path = CONCAT(:newPart, SUBSTRING(f.file_path, LENGTH(:oldPart) + 1)) " +
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
   @Query("UPDATE File f SET f.deletedAt = CURRENT_TIMESTAMP, " +
           "f.filePath = CONCAT(:newPart, SUBSTRING(f.filePath, LENGTH(:oldPart) + 1)) " +
           "WHERE f.folderId IN (" +
           "SELECT fo.folderId FROM Folder fo WHERE fo.projectId = :projectId)")
   void softDeleteByProjectId(@Param("projectId") Integer projectId,@Param("oldPart") String oldPart, @Param("newPart") String newPart);

   List<File> findByFolderIdIn(List<Integer> folderIds);

}
