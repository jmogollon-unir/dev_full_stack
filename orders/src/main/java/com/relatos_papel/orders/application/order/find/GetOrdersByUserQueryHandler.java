package com.relatos_papel.orders.application.order.find;

import com.relatos_papel.orders.application.order.common.OrderDto;
import com.relatos_papel.orders.common.mediator.RequestHandler;
import com.relatos_papel.orders.infrastructure.repositories.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class GetOrdersByUserQueryHandler implements RequestHandler<GetOrdersByUserQuery, List<OrderDto>> {

    private final OrderRepository orderRepository;

    @Override
    public List<OrderDto> handle(GetOrdersByUserQuery request) {
        return orderRepository.findByUserIdOrderByOrderDateDesc(request.getUserId())
                .stream()
                .map(OrderDto::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    public Class<GetOrdersByUserQuery> getRequestType() {
        return GetOrdersByUserQuery.class;
    }
}
