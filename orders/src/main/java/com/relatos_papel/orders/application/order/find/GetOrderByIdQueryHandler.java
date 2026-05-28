package com.relatos_papel.orders.application.order.find;

import com.relatos_papel.orders.application.order.common.OrderDto;
import com.relatos_papel.orders.application.order.common.OrderEnrichmentService;
import com.relatos_papel.orders.common.exception.ResourceNotFoundException;
import com.relatos_papel.orders.common.mediator.RequestHandler;
import com.relatos_papel.orders.infrastructure.repositories.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class GetOrderByIdQueryHandler implements RequestHandler<GetOrderByIdQuery, OrderDto> {

    private final OrderRepository orderRepository;
    private final OrderEnrichmentService orderEnrichmentService;

    @Override
    public OrderDto handle(GetOrderByIdQuery request) {
        var order = orderRepository.findById(request.getOrderId())
                .orElseThrow(() -> new ResourceNotFoundException("No se encontraron pedidos con el ID " + request.getOrderId()));
        OrderDto dto = OrderDto.mapToDto(order);
        return orderEnrichmentService.enrich(dto);
    }

    @Override
    public Class<GetOrderByIdQuery> getRequestType() {
        return GetOrderByIdQuery.class;
    }
}
