package com.marginalia.marginalia.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Table(name = "user_books")
@Data
public class UserBook {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Relationship to User
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    // Relationship to Book
    @ManyToOne
    @JoinColumn(name = "book_id", nullable = false)
    private Book book;

    // THIS IS THE KEY FOR SPOILER PREVENTION!
    @Column(name = "current_page", nullable = false)
    private Integer currentPage = 0;  // Default: just started

    @Column(name = "date_started")
    private LocalDateTime dateStarted;

    @Column(name = "date_finished")
    private LocalDateTime dateFinished;

    @Column(name = "is_finished")
    private Boolean isFinished = false;

    // Computed property: reading progress percentage
    @Transient  // Not stored in database, calculated on the fly
    public Integer getProgressPercentage() {
        if (book == null || book.getTotalPages() == null || book.getTotalPages() == 0) {
            return 0;
        }
        return (currentPage * 100) / book.getTotalPages();
    }

    @PrePersist
    protected void onCreate() {
        dateStarted = LocalDateTime.now();
    }
}