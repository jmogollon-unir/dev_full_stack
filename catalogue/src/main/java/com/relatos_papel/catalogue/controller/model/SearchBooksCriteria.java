package com.relatos_papel.catalogue.controller.model;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
public class SearchBooksCriteria {
    private String title;
    private String author;
    private String isbn;
    private Integer category;
    private String categoryName;
    private LocalDate publicationDate;
    private Integer rating;
    private Boolean visible;
    private Integer popularity;
}
