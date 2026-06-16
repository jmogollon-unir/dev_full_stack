package com.relatos_papel.orders.event.service;

import com.relatos_papel.orders.event.model.EventHeader;
import com.relatos_papel.orders.event.model.OrderCreatedEvent;
import com.relatos_papel.orders.event.model.OrderCreatedEventBody;
import com.relatos_papel.orders.event.model.OrderItemEvent;
import com.relatos_papel.orders.repository.model.Order;
import com.relatos_papel.orders.repository.model.OrderItem;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderEventService {

    private final RabbitTemplate rabbitTemplate;

    @Value("${rabbitmq.exchange.orders}")
    private String ordersExchange;

    @Value("${rabbitmq.routing.key.order.created}")
    private String orderCreatedRoutingKey;

    public void publishOrderCreatedEvent(Order order, String email) {
        try {
            OrderCreatedEvent event = buildOrderCreatedEvent(order, email);
            rabbitTemplate.convertAndSend(ordersExchange, orderCreatedRoutingKey, event);
            log.info("Evento de pedido creado publicado exitosamente. OrderId: {}, EventId: {}", order.getOrderId(), event.getHeader().getEventId());
        } catch (Exception e) {
            log.error("Error al publicar evento de pedido creado para orderId: {}", order.getOrderId(), e);
        }
    }

    private OrderCreatedEvent buildOrderCreatedEvent(Order order, String email) {
        String eventId = UUID.randomUUID().toString();

        EventHeader header = EventHeader.builder()
                .eventId(eventId)
                .version("1.0")
                .eventType("ORDER_CREATED")
                .timestamp(LocalDateTime.now())
                .build();

        List<OrderItemEvent> orderItemEvents = order.getOrderItems().stream()
                .map(this::mapToOrderItemEvent)
                .toList();

        OrderCreatedEventBody body = OrderCreatedEventBody.builder()
                .orderId(order.getOrderId())
                .userId(order.getUserId())
                .email(email)
                .orderDate(order.getOrderDate())
                .total(order.getTotal())
                .statusId(order.getStatusId())
                .address(order.getAddress())
                .city(order.getCity())
                .country(order.getCountry())
                .phone(order.getPhone())
                .orderItems(orderItemEvents)
                .build();

        return OrderCreatedEvent.builder()
                .header(header)
                .body(body)
                .build();
    }

    private OrderItemEvent mapToOrderItemEvent(OrderItem orderItem) {
        return OrderItemEvent.builder()
                .bookId(orderItem.getBookId())
                .quantity(orderItem.getQuantity())
                .price(orderItem.getPrice())
                .subTotal(orderItem.getPrice().multiply(java.math.BigDecimal.valueOf(orderItem.getQuantity())))
                .build();
    }
}
