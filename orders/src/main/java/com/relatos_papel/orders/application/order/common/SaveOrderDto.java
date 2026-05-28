package com.relatos_papel.orders.application.order.common;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class SaveOrderDto {

    private Long userId;
    private String address;
    private String city;
    private String country;
    private String phone;
    private List<OrderItemInput> items;

    @Data
    public static class OrderItemInput {
        private Long bookId;
        private Integer quantity;
        private BigDecimal price;
    }
}
