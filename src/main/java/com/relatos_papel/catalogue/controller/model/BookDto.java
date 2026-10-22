package com.relatos_papel.catalogue.controller.model;

import com.relatos_papel.catalogue.repository.model.Book;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Builder
public class BookDto {
    private Integer bookId;
    private String title;
    private String author;
    private String isbn;
    private BigDecimal price;
    private Integer stock;
    private String coverUrl;
    private String description;
    private String genreName;
    private Integer genreId;
    private String format;
    private String language;
    private LocalDate publicationDate;
    private Integer popularity;
    private Boolean isAvailable;

    public static BookDto mapToDto(Book book) {
        return BookDto.builder()
                .bookId(book.getBookId())
                .title(book.getTitle())
                .author(book.getAuthor())
                .isbn(book.getIsbn())
                .price(book.getPrice())
                .stock(book.getStock())
                .coverUrl(book.getCoverUrl())
                .description(book.getDescription())
                .genreId(book.getGenre().getGenreId())
                .genreName(book.getGenre().getName())
                .format(book.getFormat() != null ? book.getFormat().getValue() : null)
                .language(book.getLanguage())
                .publicationDate(book.getPublicationDate())
                .popularity(book.getPopularity())
                .isAvailable(book.getIsAvailable())
                .build();
    }
}
