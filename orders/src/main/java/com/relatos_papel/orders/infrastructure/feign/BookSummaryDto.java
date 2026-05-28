package com.relatos_papel.orders.infrastructure.feign;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class BookSummaryDto {
    private Long bookId;
    private String title;
    private String author;
    private String isbn;
    private BigDecimal price;
    private Integer stock;
    private Boolean isAvailable; // necesario para validar que el libro no esté oculto
}
