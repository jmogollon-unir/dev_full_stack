package com.relatos_papel.orders.infrastructure.feign;

import lombok.Data;

@Data
public class StockDecreaseDto {
    private Integer quantity;
}
