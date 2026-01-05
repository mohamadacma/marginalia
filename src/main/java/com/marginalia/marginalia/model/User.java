package com.marginalia.marginalia.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;


@Entity
@Table( name = "users")
@Data
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String username;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(name = "joined_date")
    private LocalDateTime joinedDate;

    @PrePersist
    protected void onCreate() {
        joinedDate = LocalDateTime.now();
    }
}
