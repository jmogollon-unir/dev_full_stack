package com.relatos_papel.orders.controller.model;

import lombok.Data;

import java.util.List;

@Data
public class CreateOrderRequestDto {

    private Integer userId;
    private List<RequestedBook> items;

    @Data
    public static class RequestedBook {
        private Integer bookId;
        private Integer quantity;
    }
}
