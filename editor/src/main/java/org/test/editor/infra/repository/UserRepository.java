package org.test.editor.infra.repository;


import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.test.editor.core.dto.UserInvitationDto;
import org.test.editor.core.model.User;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    Optional<User> findByName(String userName);
    Optional<User> findByEmail(String email);
    boolean existsByEmail(String email);
    @Query("SELECT u " +
            "FROM Collaborator c " +
            "JOIN c.user u " +
            "WHERE c.projectId = :projectId AND c.collaboratorRoleId = :roleId")
    Optional<User> findUserByProjectIdAndRoleId(@Param("projectId") Integer projectId, @Param("roleId") Integer roleId);
    @Query("SELECT new org.test.editor.core.dto.UserInvitationDto(u.userId, u.name, u.email) " +
            "FROM User u " +
            "WHERE u.name LIKE %:searchTerm% OR u.email LIKE %:searchTerm%")
    List<UserInvitationDto> findUsersByNameOrEmail(@Param("searchTerm") String searchTerm, Pageable pageable);
}