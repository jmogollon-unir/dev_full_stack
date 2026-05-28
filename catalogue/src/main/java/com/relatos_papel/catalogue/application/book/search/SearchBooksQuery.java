package com.relatos_papel.catalogue.application.book.search;

import com.relatos_papel.catalogue.application.book.common.BookDto;
import com.relatos_papel.catalogue.common.mediator.Request;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SearchBooksQuery implements Request<List<BookDto>> {
    private String title;
    private String author;
    private String isbn;
    private Long category; // ID del género
    private LocalDate publicationDate;
    private Integer rating; // de 1 a 5
    private Boolean visible; // isAvailable
    private Integer popularity;
}