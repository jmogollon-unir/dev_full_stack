package com.relatos_papel.orders.application.order.find;

import com.relatos_papel.orders.application.order.common.OrderDto;
import com.relatos_papel.orders.application.order.common.OrderEnrichmentService;
import com.relatos_papel.orders.common.mediator.RequestHandler;
import com.relatos_papel.orders.infrastructure.repositories.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class GetOrdersByUserQueryHandler implements RequestHandler<GetOrdersByUserQuery, List<OrderDto>> {

    private final OrderRepository orderRepository;
    private final OrderEnrichmentService orderEnrichmentService;

    @Override
    public List<OrderDto> handle(GetOrdersByUserQuery request) {
        List<OrderDto> orders = orderRepository.findByUserIdOrderByOrderDateDesc(request.getUserId())
                .stream()
                .map(OrderDto::mapToSummaryDto)
                .toList();

        return orderEnrichmentService.enrichAll(orders);
    }

    @Override
    public Class<GetOrdersByUserQuery> getRequestType() {
        return GetOrdersByUserQuery.class;
    }
}
