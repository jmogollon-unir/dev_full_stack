package com.relatos_papel.orders.application.order.common;

import com.relatos_papel.orders.domain.model.Order;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Data
@Builder
public class OrderDto {

    private Long orderId;
    private Long userId;
    private String address;
    private String city;
    private String country;
    private String phone;
    private Integer statusId;
    private BigDecimal total;
    private LocalDateTime orderDate;
    private LocalDateTime createdAt;
    private List<OrderItemDto> items;

    public static OrderDto mapToDto(Order order) {
        return OrderDto.builder()
                .orderId(order.getOrderId())
                .userId(order.getUserId())
                .address(order.getAddress())
                .city(order.getCity())
                .country(order.getCountry())
                .phone(order.getPhone())
                .statusId(order.getStatusId())
                .total(order.getTotal())
                .orderDate(order.getOrderDate())
                .createdAt(order.getCreatedAt())
                .items(order.getItems() != null
                        ? order.getItems().stream().map(OrderItemDto::mapToDto).collect(Collectors.toList())
                        : List.of())
                .build();
    }
}
