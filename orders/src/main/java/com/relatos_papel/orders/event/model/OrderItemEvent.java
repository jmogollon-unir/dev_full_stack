package com.relatos_papel.orders.event.model;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class OrderItemEvent {
    private Integer bookId;
    private Integer quantity;
    private BigDecimal price;
    private BigDecimal subTotal;
}
