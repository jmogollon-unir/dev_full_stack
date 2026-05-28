package com.relatos_papel.orders.application.order.create;

import com.relatos_papel.orders.application.order.common.OrderDto;
import com.relatos_papel.orders.application.order.common.SaveOrderDto;
import com.relatos_papel.orders.common.mediator.Request;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CreateOrderCommand implements Request<OrderDto> {
    private SaveOrderDto data;
}
