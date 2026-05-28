package com.relatos_papel.orders.application.order.common;

import com.relatos_papel.orders.domain.model.OrderItem;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class OrderItemDto {

    private Long orderItemsId;
    private Long bookId;
    private String bookTitle;   // enriquecido via Feign desde catalogue
    private Integer quantity;
    private BigDecimal price;
    private BigDecimal subtotal;

    public static OrderItemDto mapToDto(OrderItem item) {
        return OrderItemDto.builder()
                .orderItemsId(item.getOrderItemsId())
                .bookId(item.getBookId())
                .bookTitle(null)   // se enriquece en el handler si se necesita
                .quantity(item.getQuantity())
                .price(item.getPrice())
                .subtotal(item.getPrice().multiply(BigDecimal.valueOf(item.getQuantity())))
                .build();
    }
}
