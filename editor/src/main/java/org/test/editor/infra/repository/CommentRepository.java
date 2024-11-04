package org.test.editor.infra.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.test.editor.core.dto.CommentDTO;
import org.test.editor.core.model.Comment;

import java.util.List;
import java.util.Optional;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Integer> {


    @Query("SELECT new org.test.editor.core.dto.CommentDTO(c.commentId, c.content, u.name, c.createdAt,c.userId) " +
            "FROM Comment c JOIN c.user u WHERE c.discussionId = :discussionId")
    List<CommentDTO> findCommentsByDiscussionId(@Param("discussionId") Integer discussionId);

    @Query("SELECT new org.test.editor.core.dto.CommentDTO(c.commentId, c.content, u.name, null ,c.userId) " +
            "FROM Comment c JOIN c.user u WHERE c.commentId = :commentId")
    Optional<CommentDTO> findByCommentId(@Param("commentId") Integer commentId );
}