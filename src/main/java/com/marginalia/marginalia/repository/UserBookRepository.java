package com.marginalia.marginalia.repository;

import com.marginalia.marginalia.model.UserBook;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserBookRepository extends JpaRepository<UserBook, Long> {

    // Find all books a user is reading
    List<UserBook> findByUserId(Long userId);

    // Find all users reading a specific book
    List<UserBook> findByBookId(Long bookId);

    // Find a specific user's relationship with a specific book
    Optional<UserBook> findByUserIdAndBookId(Long userId, Long bookId);

    // Find books user hasn't finished
    List<UserBook> findByUserIdAndIsFinished(Long userId, Boolean isFinished);

    // Check if user is reading this book
    boolean existsByUserIdAndBookId(Long userId, Long bookId);
}