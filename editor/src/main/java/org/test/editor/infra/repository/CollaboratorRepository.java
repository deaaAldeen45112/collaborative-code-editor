package org.test.editor.infra.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.test.editor.core.model.Collaborator;

@Repository
public interface CollaboratorRepository extends JpaRepository<Collaborator, Integer> {

    boolean existsByUserIdAndProjectId(Integer userId, Integer projectId);

    @Query("SELECT CASE WHEN COUNT(c) > 0 THEN true ELSE false END " +
            "FROM Collaborator c " +
            "JOIN c.user u " +
            "JOIN c.project p " +
            "WHERE u.name = :userName AND p.projectName = :projectName")
    boolean existsByUserNameAndProjectName(@Param("userName") String userName, @Param("projectName") String projectName);

    @Query("SELECT CASE WHEN COUNT(c) > 0 THEN true ELSE false END " +
            "FROM Collaborator c " +
            "JOIN c.project p " +
            "WHERE c.userId = :userId AND p.projectName = :projectName")
    boolean existsByUserIdAndProjectName(@Param("userId") Integer userId, @Param("projectName") String projectName);

    @Query("SELECT CASE WHEN COUNT(c.collaboratorId) > 0 THEN true ELSE false END " +
            "FROM Collaborator c " +
            "JOIN c.user u " +
            "WHERE u.name = :userName AND c.projectId = :projectId")
    boolean existsByUserNameAndProjectId(@Param("userName") String userName, @Param("projectId") Integer projectId);

    @Query("SELECT CASE WHEN COUNT(c.collaboratorId) > 0 THEN true ELSE false END " +
            "FROM Collaborator c " +
            "JOIN c.user u " +
            "WHERE u.email = :email AND c.projectId = :projectId")
    boolean existsByEmailAndProjectId(@Param("email") String email, @Param("projectId") Integer projectId);

    @Query("SELECT CASE WHEN COUNT(c.collaboratorId) > 0 THEN true ELSE false END " +
            "FROM Collaborator c " +
            "JOIN c.user u " +
            "WHERE u.userId=:userId AND c.projectId = :projectId AND c.collaboratorRoleId = :roleId")
    boolean isUserProjectOwner(@Param("userId") Integer userId, @Param("projectId") Integer projectId, @Param("roleId") Integer roleId);


}