package com.relatos_papel.orders.event.model;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
public class OrderCreatedEventBody {
    private Integer orderId;
    private Integer userId;
    private String email;
    private LocalDateTime orderDate;
    private BigDecimal total;
    private Integer statusId;
    private String address;
    private String city;
    private String country;
    private String phone;
    private List<OrderItemEvent> orderItems;
}
