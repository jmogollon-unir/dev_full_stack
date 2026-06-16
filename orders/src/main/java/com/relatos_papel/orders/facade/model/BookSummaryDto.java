package com.relatos_papel.orders.facade.model;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class BookSummaryDto {
    private Integer bookId;
    private String title;
    private String author;
    private String isbn;
    private BigDecimal price;
    private Integer stock;
    private Boolean isAvailable;
}
