package com.relatos_papel.catalogue.search.model;

import com.relatos_papel.catalogue.controller.model.BookDto;
import com.relatos_papel.catalogue.repository.model.Book;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.DateFormat;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.math.BigDecimal;
import java.time.LocalDate;

@Document(indexName = "books")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BookSearchDocument {

    @Id
    private Integer bookId;

    @Field(type = FieldType.Text)
    private String title;

    @Field(type = FieldType.Text)
    private String author;

    @Field(type = FieldType.Keyword)
    private String isbn;

    @Field(type = FieldType.Double)
    private BigDecimal price;

    @Field(type = FieldType.Integer)
    private Integer stock;

    @Field(type = FieldType.Keyword, index = false)
    private String coverUrl;

    @Field(type = FieldType.Text)
    private String description;

    @Field(type = FieldType.Integer)
    private Integer genreId;

    @Field(type = FieldType.Text)
    private String genreName;

    @Field(type = FieldType.Keyword)
    private String format;

    @Field(type = FieldType.Keyword)
    private String language;

    @Field(type = FieldType.Date, format = DateFormat.date)
    private LocalDate publicationDate;

    @Field(type = FieldType.Integer)
    private Integer popularity;

    @Field(type = FieldType.Boolean)
    private Boolean isAvailable;

    public static BookSearchDocument fromBook(Book book) {
        return BookSearchDocument.builder()
                .bookId(book.getBookId())
                .title(book.getTitle())
                .author(book.getAuthor())
                .isbn(book.getIsbn())
                .price(book.getPrice())
                .stock(book.getStock())
                .coverUrl(book.getCoverUrl())
                .description(book.getDescription())
                .genreId(book.getGenre() != null ? book.getGenre().getGenreId() : null)
                .genreName(book.getGenre() != null ? book.getGenre().getName() : null)
                .format(book.getFormat() != null ? book.getFormat().getValue() : null)
                .language(book.getLanguage())
                .publicationDate(book.getPublicationDate())
                .popularity(book.getPopularity())
                .isAvailable(book.getIsAvailable())
                .build();
    }

    public BookDto toDto() {
        return BookDto.builder()
                .bookId(bookId)
                .title(title)
                .author(author)
                .isbn(isbn)
                .price(price)
                .stock(stock)
                .coverUrl(coverUrl)
                .description(description)
                .genreId(genreId)
                .genreName(genreName)
                .format(format)
                .language(language)
                .publicationDate(publicationDate)
                .popularity(popularity)
                .isAvailable(isAvailable)
                .build();
    }
}
