package com.marginalia.marginalia.repository;

import com.marginalia.marginalia.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {

    // Find book by ISBN
    Optional<Book> findByIsbn(String isbn);

    // Find books by author
    List<Book> findByAuthor(String author);

    // Find books by title (partial match)
    List<Book> findByTitleContainingIgnoreCase(String title);

    // Check if ISBN exists
    boolean existsByIsbn(String isbn);
}