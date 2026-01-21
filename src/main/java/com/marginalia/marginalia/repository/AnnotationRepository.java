package com.marginalia.marginalia.repository;

import com.marginalia.marginalia.model.Annotation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AnnotationRepository extends JpaRepository<Annotation, Long> {

    // Get all annotations for a book (no filtering - for admin/testing)
    List<Annotation> findByBookIdOrderByPageNumberAsc(Long bookId);

    // Get annotations by a specific user for a specific book
    List<Annotation> findByUserIdAndBookIdOrderByPageNumberAsc(Long userId, Long bookId);

    // THE SPOILER-PREVENTION QUERY!
    // Only get annotations up to the user's current page
    @Query("SELECT a FROM Annotation a " +
            "WHERE a.book.id = :bookId " +
            "AND a.pageNumber <= :currentPage " +
            "ORDER BY a.pageNumber ASC")
    List<Annotation> findSafeAnnotations(
            @Param("bookId") Long bookId,
            @Param("currentPage") Integer currentPage
    );
    // Get most liked annotations across all books
    @Query("SELECT a FROM Annotation a " +
            "LEFT JOIN Like l ON l.annotation.id = a.id " +
            "GROUP BY a.id " +
            "ORDER BY COUNT(l) DESC")
    List<Annotation> findMostLiked();

    // Get most liked annotations for a specific book
    @Query("SELECT a FROM Annotation a " +
            "LEFT JOIN Like l ON l.annotation.id = a.id " +
            "WHERE a.book.id = :bookId " +
            "GROUP BY a.id " +
            "ORDER BY COUNT(l) DESC")
    List<Annotation> findMostLikedByBook(@Param("bookId") Long bookId);

    // Get annotations with like count (custom projection)
    @Query("SELECT a.id as annotationId, a.text as text, " +
            "a.pageNumber as pageNumber, COUNT(l) as likeCount " +
            "FROM Annotation a " +
            "LEFT JOIN Like l ON l.annotation.id = a.id " +
            "GROUP BY a.id, a.text, a.pageNumber " +
            "ORDER BY COUNT(l) DESC")
    List<Object[]> findAnnotationsWithLikeCount();

    // Get annotations from friends (for feed feature later)
    @Query("SELECT a FROM Annotation a " +
            "WHERE a.user.id IN :friendIds " +
            "AND a.book.id = :bookId " +
            "AND a.pageNumber <= :currentPage " +
            "ORDER BY a.createdAt DESC")
    List<Annotation> findFriendsAnnotations(
            @Param("friendIds") List<Long> friendIds,
            @Param("bookId") Long bookId,
            @Param("currentPage") Integer currentPage
    );

    // Get recent annotations from friends (feed feature)
    @Query("SELECT a FROM Annotation a " +
            "WHERE a.user.id IN :friendIds " +
            "ORDER BY a.createdAt DESC")
    List<Annotation> findRecentFriendsAnnotations(@Param("friendIds") List<Long> friendIds);

    // Get recent annotations from a user
    List<Annotation> findByUserIdOrderByCreatedAtDesc(Long userId);
}