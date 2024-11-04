package org.test.editor.infra.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.test.editor.core.dto.InvitationDTO;
import org.test.editor.core.model.Invitation;

import java.util.List;

@Repository
public interface InvitationRepository extends JpaRepository<Invitation, Integer> {

    @Query("SELECT new org.test.editor.core.dto.InvitationDTO(" +
            "i.invitationId, " +
            "u.name, " +
            "p.projectName, " +
            "s.statusName, " +
            "i.invitationSentAt, " +
            "i.expiresAt, " +
            "s.statusId, " +
            "i.acceptedAt) " +
            "FROM Invitation i " +
            "JOIN i.user u " +
            "JOIN i.project p " +
            "JOIN i.status s " +
            "WHERE u.userId = :userId")
    List<InvitationDTO> findAllInvitationsByUserId(@Param("userId") Integer userId);

    @Query("SELECT new org.test.editor.core.dto.InvitationDTO(" +
            "i.invitationId, " +
            "u.name, " +
            "p.projectName, " +
            "s.statusName, " +
            "i.invitationSentAt, " +
            "i.expiresAt, " +
            "s.statusId, " +
            "i.acceptedAt) " +
            "FROM Invitation i " +
            "JOIN i.user u " +
            "JOIN i.project p " +
            "JOIN p.collaborators c " +
            "JOIN i.status s " +
            "WHERE c.userId = :userId AND c.collaboratorRoleId=1")
    List<InvitationDTO> findSentInvitations(@Param("userId") Integer userId);



}
