package com.relatos_papel.catalogue.domain.model;

import com.relatos_papel.catalogue.domain.model.enums.BookFormat;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "books", indexes = {
        @Index(name = "idx_title", columnList = "title"),
        @Index(name = "idx_author", columnList = "author"),
        @Index(name = "idx_price", columnList = "price"),
        @Index(name = "idx_publication_date", columnList = "publication_date"),
        @Index(name = "idx_popularity", columnList = "popularity")
})
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "book_id")
    private Integer bookId;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String author;

    @Column(unique = true, nullable = false, length = 20)
    private String isbn;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal price;

    @Column(nullable = false)
    private Integer stock = 0;

    @Lob
    @Column(name = "cover_url", columnDefinition = "LONGTEXT")
    private String coverUrl;

    @Lob
    @Column(columnDefinition = "LONGTEXT")
    private String description;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "genre_id", nullable = false)
    private Genre genre;

    @Column(name = "format")
    private BookFormat format = BookFormat.AMBOS;

    @Column(length = 50)
    private String language = "Español";

    @Column(name = "publication_date")
    private LocalDate publicationDate;

    private Integer popularity = 0;

    @Column(name = "is_available")
    private Boolean isAvailable = true;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}
