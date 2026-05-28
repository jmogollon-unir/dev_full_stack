package com.relatos_papel.orders.application.order.find;

import com.relatos_papel.orders.application.order.common.OrderDto;
import com.relatos_papel.orders.common.mediator.Request;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class GetOrdersByUserQuery implements Request<List<OrderDto>> {
    private Integer userId;
}
