package com.relatos_papel.communications.events.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderItemEvent {
    private Integer bookId;
    private Integer quantity;
    private BigDecimal price;
    private BigDecimal subTotal;
}
