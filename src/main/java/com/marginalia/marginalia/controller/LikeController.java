package com.marginalia.marginalia.controller;

import com.marginalia.marginalia.dto.LikeRequest;
import com.marginalia.marginalia.model.Annotation;
import com.marginalia.marginalia.model.Like;
import com.marginalia.marginalia.model.User;
import com.marginalia.marginalia.repository.AnnotationRepository;
import com.marginalia.marginalia.repository.LikeRepository;
import com.marginalia.marginalia.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/likes")
public class LikeController {

    @Autowired
    private LikeRepository likeRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AnnotationRepository annotationRepository;

    // Like an annotation
    @PostMapping
    public ResponseEntity<?> likeAnnotation(@RequestBody LikeRequest request) {
        // Validate user exists
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Validate annotation exists
        Annotation annotation = annotationRepository.findById(request.getAnnotationId())
                .orElseThrow(() -> new RuntimeException("Annotation not found"));

        // Check if already liked
        if (likeRepository.existsByUserIdAndAnnotationId(request.getUserId(), request.getAnnotationId())) {
            return ResponseEntity.badRequest().body("Already liked this annotation");
        }

        // Create like
        Like like = new Like();
        like.setUser(user);
        like.setAnnotation(annotation);

        return ResponseEntity.ok(likeRepository.save(like));
    }

    // Unlike an annotation
    @DeleteMapping
    public ResponseEntity<?> unlikeAnnotation(@RequestBody LikeRequest request) {
        Like like = likeRepository.findByUserIdAndAnnotationId(
                request.getUserId(),
                request.getAnnotationId()
        ).orElse(null);

        if (like == null) {
            return ResponseEntity.notFound().build();
        }

        likeRepository.delete(like);
        return ResponseEntity.ok().build();
    }

    // Get like count for an annotation
    @GetMapping("/annotation/{annotationId}/count")
    public ResponseEntity<Map<String, Long>> getLikeCount(@PathVariable Long annotationId) {
        long count = likeRepository.countByAnnotationId(annotationId);
        Map<String, Long> response = new HashMap<>();
        response.put("count", count);
        return ResponseEntity.ok(response);
    }

    // Get all users who liked an annotation
    @GetMapping("/annotation/{annotationId}")
    public ResponseEntity<List<Like>> getAnnotationLikes(@PathVariable Long annotationId) {
        return ResponseEntity.ok(likeRepository.findByAnnotationId(annotationId));
    }

    // Check if user liked an annotation
    @GetMapping("/check")
    public ResponseEntity<Map<String, Boolean>> checkIfLiked(
            @RequestParam Long userId,
            @RequestParam Long annotationId) {
        boolean liked = likeRepository.existsByUserIdAndAnnotationId(userId, annotationId);
        Map<String, Boolean> response = new HashMap<>();
        response.put("liked", liked);
        return ResponseEntity.ok(response);
    }
}