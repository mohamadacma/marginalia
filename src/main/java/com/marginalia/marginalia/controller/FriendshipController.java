package com.marginalia.marginalia.controller;

import com.marginalia.marginalia.dto.AddFriendRequest;
import com.marginalia.marginalia.model.Friendship;
import com.marginalia.marginalia.model.User;
import com.marginalia.marginalia.repository.FriendshipRepository;
import com.marginalia.marginalia.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/friendships")
public class FriendshipController {

    @Autowired
    private FriendshipRepository friendshipRepository;

    @Autowired
    private UserRepository userRepository;

    // Add a friend (creates bidirectional relationship)
    @PostMapping
    public ResponseEntity<?> addFriend(@RequestBody AddFriendRequest request) {
        // Validate users exist
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        User friend = userRepository.findById(request.getFriendId())
                .orElseThrow(() -> new RuntimeException("Friend not found"));

        // Can't friend yourself
        if (request.getUserId().equals(request.getFriendId())) {
            return ResponseEntity.badRequest().body("Cannot add yourself as a friend");
        }

        // Check if already friends
        if (friendshipRepository.existsByUserIdAndFriendId(request.getUserId(), request.getFriendId())) {
            return ResponseEntity.badRequest().body("Already friends");
        }

        // Create bidirectional friendship
        Friendship friendship1 = new Friendship();
        friendship1.setUser(user);
        friendship1.setFriend(friend);
        friendshipRepository.save(friendship1);

        Friendship friendship2 = new Friendship();
        friendship2.setUser(friend);
        friendship2.setFriend(user);
        friendshipRepository.save(friendship2);

        return ResponseEntity.ok(friendship1);
    }

    // Get all friends of a user
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<User>> getUserFriends(@PathVariable Long userId) {
        List<Friendship> friendships = friendshipRepository.findByUserId(userId);

        // Extract just the friend objects
        List<User> friends = friendships.stream()
                .map(Friendship::getFriend)
                .collect(Collectors.toList());

        return ResponseEntity.ok(friends);
    }

    // Get friend count
    @GetMapping("/user/{userId}/count")
    public ResponseEntity<Long> getFriendCount(@PathVariable Long userId) {
        return ResponseEntity.ok(friendshipRepository.countByUserId(userId));
    }

    // Remove friend (removes both directions)
    @DeleteMapping
    public ResponseEntity<?> removeFriend(@RequestBody AddFriendRequest request) {
        // Find both friendships
        Friendship friendship1 = friendshipRepository
                .findByUserIdAndFriendId(request.getUserId(), request.getFriendId())
                .orElse(null);

        Friendship friendship2 = friendshipRepository
                .findByUserIdAndFriendId(request.getFriendId(), request.getUserId())
                .orElse(null);

        if (friendship1 == null && friendship2 == null) {
            return ResponseEntity.notFound().build();
        }

        // Delete both
        if (friendship1 != null) friendshipRepository.delete(friendship1);
        if (friendship2 != null) friendshipRepository.delete(friendship2);

        return ResponseEntity.ok().build();
    }
}