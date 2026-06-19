package com.relatos_papel.communications.events.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
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
