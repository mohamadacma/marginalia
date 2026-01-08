package com.marginalia.marginalia.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Table(name = "books")
@Data
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String author;

    @Column(unique = true)
    private String isbn;  // International Standard Book Number

    @Column(name = "total_pages")
    private Integer totalPages;

    @Column(name = "cover_image_url")
    private String coverImageUrl;  //  book cover

    @Column(name = "date_added")
    private LocalDateTime dateAdded;

    @PrePersist
    protected void onCreate() {
        dateAdded = LocalDateTime.now();
    }
}