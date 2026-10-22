package com.relatos_papel.catalogue.controller.model;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class SaveBookDto {
    private String title;
    private String author;
    private String isbn;
    private BigDecimal price;
    private Integer stock;
    private String coverUrl;
    private String description;
    private Integer genreId;
    private String format;
    private String language;
    private LocalDate publicationDate;
    private Boolean isAvailable;
}
