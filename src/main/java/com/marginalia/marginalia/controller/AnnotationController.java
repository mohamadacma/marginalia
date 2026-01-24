package com.marginalia.marginalia.controller;

import com.marginalia.marginalia.dto.AnnotationWithLikesDTO;
import com.marginalia.marginalia.model.Annotation;
import com.marginalia.marginalia.model.Book;
import com.marginalia.marginalia.model.User;
import com.marginalia.marginalia.model.UserBook;
import com.marginalia.marginalia.repository.AnnotationRepository;
import com.marginalia.marginalia.repository.BookRepository;
import com.marginalia.marginalia.repository.UserRepository;
import com.marginalia.marginalia.repository.UserBookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.marginalia.marginalia.repository.FriendshipRepository;
import com.marginalia.marginalia.service.AnnotationService;


import java.util.List;

@RestController
@RequestMapping("/api/annotations")
public class AnnotationController {

    @Autowired
    private AnnotationRepository annotationRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private UserBookRepository userBookRepository;
    @Autowired
    private FriendshipRepository friendshipRepository;

    // Create a new annotation
    @PostMapping
    public ResponseEntity<?> createAnnotation(@RequestBody Annotation annotation) {
        // Validate user exists
        if (annotation.getUser() == null || annotation.getUser().getId() == null) {
            return ResponseEntity.badRequest().body("User ID is required");
        }

        if (annotation.getBook() == null || annotation.getBook().getId() == null) {
            return ResponseEntity.badRequest().body("Book ID is required");
        }

        Long userId = annotation.getUser().getId();
        Long bookId = annotation.getBook().getId();

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new RuntimeException("Book not found"));

        // Check if user is actually reading this book
        UserBook userBook = userBookRepository
                .findByUserIdAndBookId(userId, bookId)
                .orElseThrow(() -> new RuntimeException("User is not reading this book"));

        // ✅ THE FIX: Prevent annotating beyond current progress
        if (annotation.getPageNumber() > userBook.getCurrentPage()) {
            return ResponseEntity.badRequest()
                    .body("Cannot annotate page " + annotation.getPageNumber() +
                            ". You're only on page " + userBook.getCurrentPage());
        }

        // ✅ THE FIX: Prevent annotating beyond the book's total pages
        if (book.getTotalPages() != null && annotation.getPageNumber() > book.getTotalPages()) {
            return ResponseEntity.badRequest()
                    .body("Page " + annotation.getPageNumber() +
                            " doesn't exist. Book only has " + book.getTotalPages() + " pages");
        }

        annotation.setUser(user);
        annotation.setBook(book);

        return ResponseEntity.ok(annotationRepository.save(annotation));
    }

    // Get all annotations for a book (admin view - no spoiler protection)
    // Get annotations from friends for a specific book (with spoiler protection!)
    @GetMapping("/friends/book/{bookId}")
    public ResponseEntity<List<Annotation>> getFriendsBookAnnotations(
            @PathVariable Long bookId,
            @RequestParam Long userId) {

        // Get user's friends
        List<Long> friendIds = friendshipRepository.findFriendIdsByUserId(userId);

        if (friendIds.isEmpty()) {
            return ResponseEntity.ok(List.of());  // No friends, return empty list
        }

        // Get user's progress on this book
        UserBook userBook = userBookRepository
                .findByUserIdAndBookId(userId, bookId)
                .orElseThrow(() -> new RuntimeException("User is not reading this book"));

        // Get friends' annotations (spoiler-protected!)
        List<Annotation> friendsAnnotations = annotationRepository
                .findFriendsAnnotations(friendIds, bookId, userBook.getCurrentPage());

        return ResponseEntity.ok(friendsAnnotations);
    }

    // Get recent annotations from friends (feed/timeline)
    @GetMapping("/friends/feed")
    public ResponseEntity<List<Annotation>> getFriendsFeed(@RequestParam Long userId) {
        // Get user's friends
        List<Long> friendIds = friendshipRepository.findFriendIdsByUserId(userId);

        if (friendIds.isEmpty()) {
            return ResponseEntity.ok(List.of());
        }

        // Get recent annotations from friends
        List<Annotation> feed = annotationRepository.findRecentFriendsAnnotations(friendIds);

        return ResponseEntity.ok(feed);
    }
    @GetMapping("/book/{bookId}")
    public List<Annotation> getAllBookAnnotations(@PathVariable Long bookId) {
        return annotationRepository.findByBookIdOrderByPageNumberAsc(bookId);
    }

    // Get SAFE annotations for a book based on user's progress
    // THIS IS THE SPOILER-PREVENTION ENDPOINT!
    @GetMapping("/book/{bookId}/safe")
    public ResponseEntity<List<Annotation>> getSafeAnnotations(
            @PathVariable Long bookId,
            @RequestParam Long userId) {

        // Get user's progress on this book
        UserBook userBook = userBookRepository
                .findByUserIdAndBookId(userId, bookId)
                .orElseThrow(() -> new RuntimeException("User is not reading this book"));

        // Only return annotations up to their current page!
        List<Annotation> safeAnnotations = annotationRepository
                .findSafeAnnotations(bookId, userBook.getCurrentPage());

        return ResponseEntity.ok(safeAnnotations);
    }

    // Get user's own annotations for a book
    @GetMapping("/user/{userId}/book/{bookId}")
    public List<Annotation> getUserBookAnnotations(
            @PathVariable Long userId,
            @PathVariable Long bookId) {
        return annotationRepository.findByUserIdAndBookIdOrderByPageNumberAsc(userId, bookId);
    }

    // Get user's recent annotations (for profile page)
    @GetMapping("/user/{userId}/recent")
    public List<Annotation> getUserRecentAnnotations(@PathVariable Long userId) {
        return annotationRepository.findByUserIdOrderByCreatedAtDesc(userId);
    }
    @Autowired
    private AnnotationService annotationService;

    // Get most liked annotations (global leaderboard)
    @GetMapping("/most-liked")
    public ResponseEntity<List<AnnotationWithLikesDTO>> getMostLiked(
            @RequestParam(required = false, defaultValue = "10") Integer limit) {
        return ResponseEntity.ok(annotationService.getMostLikedAnnotations(limit));
    }

    // Get most liked annotations for a specific book
    @GetMapping("/book/{bookId}/most-liked")
    public ResponseEntity<List<AnnotationWithLikesDTO>> getMostLikedByBook(
            @PathVariable Long bookId,
            @RequestParam(required = false, defaultValue = "10") Integer limit) {
        return ResponseEntity.ok(annotationService.getMostLikedByBook(bookId, limit));
    }

    // Delete annotation
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAnnotation(@PathVariable Long id) {
        if (!annotationRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        annotationRepository.deleteById(id);
        return ResponseEntity.ok().build();
    }
}