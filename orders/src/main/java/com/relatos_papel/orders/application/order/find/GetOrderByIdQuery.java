package com.relatos_papel.orders.application.order.find;

import com.relatos_papel.orders.application.order.common.OrderDto;
import com.relatos_papel.orders.common.mediator.Request;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class GetOrderByIdQuery implements Request<OrderDto> {
    private Integer orderId;
}
