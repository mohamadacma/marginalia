package com.marginalia.marginalia.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Table(name = "friendships",
        uniqueConstraints = @UniqueConstraint(columnNames = {"user_id", "friend_id"}))
@Data
public class Friendship {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // The user who initiated the friendship
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    @JsonIgnoreProperties("userBooks")
    private User user;

    // The friend
    @ManyToOne
    @JoinColumn(name = "friend_id", nullable = false)
    @JsonIgnoreProperties("userBooks")
    private User friend;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    // Optional: for future friend request system
    @Column(name = "status")
    private String status = "ACCEPTED";  // PENDING, ACCEPTED, BLOCKED

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }
}