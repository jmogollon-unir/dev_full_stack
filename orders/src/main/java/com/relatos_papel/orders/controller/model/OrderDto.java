package com.relatos_papel.orders.controller.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.relatos_papel.orders.repository.model.Order;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Data
@Builder
public class OrderDto {

    private Integer orderId;
    private String email;
    private String username;

    @JsonProperty("status_name")
    private String statusName;

    private String address;
    private String city;
    private String country;
    private String phone;
    private BigDecimal total;
    private LocalDateTime orderDate;
    private List<OrderItemDto> items;

    @JsonIgnore
    private Integer userId;

    @JsonIgnore
    private Integer statusId;

    public static OrderDto mapToDto(Order order) {
        return OrderDto.builder()
                .orderId(order.getOrderId())
                .userId(order.getUserId())
                .statusId(order.getStatusId())
                .address(order.getAddress())
                .city(order.getCity())
                .country(order.getCountry())
                .phone(order.getPhone())
                .total(order.getTotal())
                .orderDate(order.getOrderDate())
                .items(order.getOrderItems() != null
                        ? order.getOrderItems().stream().map(OrderItemDto::mapToDetailDto).collect(Collectors.toList())
                        : List.of())
                .build();
    }

    public static OrderDto mapToSummaryDto(Order order) {
        return OrderDto.builder()
                .orderId(order.getOrderId())
                .userId(order.getUserId())
                .statusId(order.getStatusId())
                .address(order.getAddress())
                .city(order.getCity())
                .country(order.getCountry())
                .phone(order.getPhone())
                .total(order.getTotal())
                .orderDate(order.getOrderDate())
                .items(order.getOrderItems() != null
                        ? order.getOrderItems().stream().map(OrderItemDto::mapToSummaryDto).collect(Collectors.toList())
                        : List.of())
                .build();
    }
}
