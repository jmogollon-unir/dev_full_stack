package com.relatos_papel.orders.application.order.status;

import com.relatos_papel.orders.application.order.common.OrderDto;
import com.relatos_papel.orders.common.mediator.RequestHandler;
import com.relatos_papel.orders.infrastructure.repositories.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UpdateOrderStatusCommandHandler implements RequestHandler<UpdateOrderStatusCommand, OrderDto> {

    private final OrderRepository orderRepository;

    @Override
    public OrderDto handle(UpdateOrderStatusCommand request) {
        var order = orderRepository.findById(request.getOrderId())
                .orElseThrow(() -> new RuntimeException("Pedido no encontrado: " + request.getOrderId()));

        order.setStatusId(request.getStatusId());
        var updated = orderRepository.save(order);
        return OrderDto.mapToDto(updated);
    }

    @Override
    public Class<UpdateOrderStatusCommand> getRequestType() {
        return UpdateOrderStatusCommand.class;
    }
}
