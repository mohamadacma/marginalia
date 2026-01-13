package com.marginalia.marginalia.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Table(name = "annotations")
@Data
public class Annotation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // The annotation text - what the user wrote
    @Column(nullable = false, length = 1000)
    private String text;

    // THE KEY FOR SPOILER PREVENTION!
    @Column(name = "page_number", nullable = false)
    private Integer pageNumber;

    // Optional: chapter reference
    private String chapter;

    // Optional: specific quote from the book
    @Column(length = 500)
    private String quote;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    // Who wrote this annotation
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    @JsonIgnoreProperties("userBooks")
    private User user;

    // Which book is this about
    @ManyToOne
    @JoinColumn(name = "book_id", nullable = false)
    @JsonIgnoreProperties("userBooks")
    private Book book;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }
}