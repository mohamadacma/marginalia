package com.marginalia.marginalia.controller;

import com.marginalia.marginalia.dto.CreateCommentRequest;
import com.marginalia.marginalia.model.Annotation;
import com.marginalia.marginalia.model.Comment;
import com.marginalia.marginalia.model.User;
import com.marginalia.marginalia.repository.AnnotationRepository;
import com.marginalia.marginalia.repository.CommentRepository;
import com.marginalia.marginalia.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/comments")
public class CommentController {

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AnnotationRepository annotationRepository;

    // Create a comment
    @PostMapping
    public ResponseEntity<?> createComment(@RequestBody CreateCommentRequest request) {
        // Validate
        if (request.getText() == null || request.getText().trim().isEmpty()) {
            return ResponseEntity.badRequest().body("Comment text is required");
        }

        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        Annotation annotation = annotationRepository.findById(request.getAnnotationId())
                .orElseThrow(() -> new RuntimeException("Annotation not found"));

        // Create comment
        Comment comment = new Comment();
        comment.setText(request.getText());
        comment.setUser(user);
        comment.setAnnotation(annotation);

        return ResponseEntity.ok(commentRepository.save(comment));
    }

    // Get all comments for an annotation
    @GetMapping("/annotation/{annotationId}")
    public ResponseEntity<List<Comment>> getAnnotationComments(@PathVariable Long annotationId) {
        return ResponseEntity.ok(
                commentRepository.findByAnnotationIdOrderByCreatedAtAsc(annotationId)
        );
    }

    // Get comment count for an annotation
    @GetMapping("/annotation/{annotationId}/count")
    public ResponseEntity<Map<String, Long>> getCommentCount(@PathVariable Long annotationId) {
        long count = commentRepository.countByAnnotationId(annotationId);
        Map<String, Long> response = new HashMap<>();
        response.put("count", count);
        return ResponseEntity.ok(response);
    }

    // Delete a comment (only by the author)
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteComment(@PathVariable Long id, @RequestParam Long userId) {
        Comment comment = commentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Comment not found"));

        // Check if user owns this comment
        if (!comment.getUser().getId().equals(userId)) {
            return ResponseEntity.status(403).body("You can only delete your own comments");
        }

        commentRepository.delete(comment);
        return ResponseEntity.ok().build();
    }
}