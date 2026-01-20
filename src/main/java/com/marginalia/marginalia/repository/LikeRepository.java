package com.marginalia.marginalia.repository;

import com.marginalia.marginalia.model.Like;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LikeRepository extends JpaRepository<Like, Long> {

    // Get all likes for an annotation
    List<Like> findByAnnotationId(Long annotationId);

    // Count likes for an annotation
    long countByAnnotationId(Long annotationId);

    // Check if user already liked an annotation
    boolean existsByUserIdAndAnnotationId(Long userId, Long annotationId);

    // Find specific like
    Optional<Like> findByUserIdAndAnnotationId(Long userId, Long annotationId);

    // Get all annotations a user has liked
    List<Like> findByUserId(Long userId);
}