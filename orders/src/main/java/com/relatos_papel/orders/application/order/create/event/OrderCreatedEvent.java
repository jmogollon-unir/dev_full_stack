package com.relatos_papel.orders.application.order.create.event;

import com.relatos_papel.orders.application.order.common.SaveOrderDto;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

import java.util.List;

@Getter
public class OrderCreatedEvent extends ApplicationEvent {
    private final List<SaveOrderDto.OrderItemInput> items;

    public OrderCreatedEvent(Object source, List<SaveOrderDto.OrderItemInput> items) {
        super(source);
        this.items = items;
    }
}

