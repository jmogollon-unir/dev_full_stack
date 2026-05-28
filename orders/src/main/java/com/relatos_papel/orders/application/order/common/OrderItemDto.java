package com.relatos_papel.orders.application.order.common;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.relatos_papel.orders.domain.model.OrderItem;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class OrderItemDto {

    private Integer orderItemsId;
    private Integer bookId;
    private String bookTitle;
    private Integer quantity;
    private BigDecimal price;
    private BigDecimal subtotal;

    public static OrderItemDto mapToDetailDto(OrderItem item) {
        return OrderItemDto.builder()
                .orderItemsId(item.getOrderItemsId())
                .bookId(item.getBookId())
                .quantity(item.getQuantity())
                .price(item.getPrice())
                .subtotal(item.getPrice().multiply(BigDecimal.valueOf(item.getQuantity())))
                .build();
    }

    public static OrderItemDto mapToSummaryDto(OrderItem item) {
        return OrderItemDto.builder()
                .bookId(item.getBookId())
                .quantity(item.getQuantity())
                .price(item.getPrice())
                .subtotal(item.getPrice().multiply(BigDecimal.valueOf(item.getQuantity())))
                .build();
    }
}
