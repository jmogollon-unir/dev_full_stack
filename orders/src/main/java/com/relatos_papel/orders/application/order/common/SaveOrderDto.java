package com.relatos_papel.orders.application.order.common;

import lombok.Data;

import java.util.List;

@Data
public class SaveOrderDto {

    private Integer userId;
    private List<OrderItemInput> items;

    @Data
    public static class OrderItemInput {
        private Integer bookId;
        private Integer quantity;
    }
}
