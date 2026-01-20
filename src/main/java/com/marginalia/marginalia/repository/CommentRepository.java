package com.marginalia.marginalia.repository;

import com.marginalia.marginalia.model.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {

    // Get all comments for an annotation (ordered by time)
    List<Comment> findByAnnotationIdOrderByCreatedAtAsc(Long annotationId);

    // Count comments for an annotation
    long countByAnnotationId(Long annotationId);

    // Get all comments by a user
    List<Comment> findByUserIdOrderByCreatedAtDesc(Long userId);
}