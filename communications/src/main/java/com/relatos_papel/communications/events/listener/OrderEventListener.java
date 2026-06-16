package com.relatos_papel.communications.events.listener;

import com.relatos_papel.communications.events.model.OrderCreatedEvent;
import com.relatos_papel.communications.events.service.EmailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class OrderEventListener {

    private final EmailService emailService;

    @RabbitListener(queues = "${rabbitmq.queue.mails.order-created}")
    public void handleOrderCreatedEvent(OrderCreatedEvent event) {
        try {
            log.info("Recibido evento de pedido creado: orderId={} - EventId: {}", event.getBody().getOrderId(), event.getHeader().getEventId());

            emailService.sendOrderCreatedNotification(event);

            log.info("Evento procesado exitosamente para el pedido: orderId={}", event.getBody().getOrderId());
        } catch (Exception e) {
            log.error("Error al procesar evento de pedido creado: orderId={} - EventId: {}", event.getBody().getOrderId(), event.getHeader().getEventId(), e);
            throw e;
        }
    }
}
