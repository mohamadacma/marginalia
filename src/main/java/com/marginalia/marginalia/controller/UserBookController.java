package com.marginalia.marginalia.controller;

import com.marginalia.marginalia.dto.StartReadingRequest;
import com.marginalia.marginalia.dto.UpdateProgressRequest;
import com.marginalia.marginalia.model.Book;
import com.marginalia.marginalia.model.User;
import com.marginalia.marginalia.model.UserBook;
import com.marginalia.marginalia.repository.BookRepository;
import com.marginalia.marginalia.repository.UserRepository;
import com.marginalia.marginalia.repository.UserBookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/user-books")
public class UserBookController {

    @Autowired
    private UserBookRepository userBookRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BookRepository bookRepository;

    // Start reading a book
    @PostMapping("/start")
    public ResponseEntity<?> startReading(@RequestBody StartReadingRequest request) {
        // Check if user exists
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Check if book exists
        Book book = bookRepository.findById(request.getBookId())
                .orElseThrow(() -> new RuntimeException("Book not found"));

        // Check if already reading
        if (userBookRepository.existsByUserIdAndBookId(request.getUserId(), request.getBookId())) {
            return ResponseEntity.badRequest().body("Already reading this book");
        }

        // Create new reading record
        UserBook userBook = new UserBook();
        userBook.setUser(user);
        userBook.setBook(book);
        userBook.setCurrentPage(0);

        return ResponseEntity.ok(userBookRepository.save(userBook));
    }

    // Get all books a user is reading
    @GetMapping("/user/{userId}")
    public List<UserBook> getUserBooks(@PathVariable Long userId) {
        return userBookRepository.findByUserId(userId);
    }

    // Get user's progress on a specific book
    @GetMapping("/user/{userId}/book/{bookId}")
    public ResponseEntity<UserBook> getUserBookProgress(
            @PathVariable Long userId,
            @PathVariable Long bookId) {
        return userBookRepository.findByUserIdAndBookId(userId, bookId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Update reading progress (THE SPOILER PREVENTION KEY!)
    @PutMapping("/{id}/progress")
    public ResponseEntity<UserBook> updateProgress(
            @PathVariable Long id,
            @RequestBody UpdateProgressRequest request) {

        UserBook userBook = userBookRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Reading record not found"));

        userBook.setCurrentPage(request.getCurrentPage());

        // Auto-mark as finished if reached the end
        if (userBook.getBook() != null &&
                request.getCurrentPage() >= userBook.getBook().getTotalPages()) {
            userBook.setIsFinished(true);
            userBook.setDateFinished(LocalDateTime.now());
        }

        return ResponseEntity.ok(userBookRepository.save(userBook));
    }

    // Get all users reading a book
    @GetMapping("/book/{bookId}/readers")
    public List<UserBook> getBookReaders(@PathVariable Long bookId) {
        return userBookRepository.findByBookId(bookId);
    }
}