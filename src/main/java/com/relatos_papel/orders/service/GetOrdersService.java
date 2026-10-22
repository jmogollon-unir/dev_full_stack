package com.relatos_papel.orders.service;

import com.relatos_papel.orders.controller.model.OrderDto;
import com.relatos_papel.orders.exception.ResourceNotFoundException;
import com.relatos_papel.orders.repository.OrderJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GetOrdersService {

    private final OrderJpaRepository orderJpaRepository;
    private final OrderEnrichmentService orderEnrichmentService;

    @Transactional(readOnly = true)
    public OrderDto getOrderById(Integer orderId) {
        var order = orderJpaRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("No se encontraron pedidos con el ID " + orderId));
        OrderDto dto = OrderDto.mapToDto(order);
        return orderEnrichmentService.enrich(dto);
    }

    @Transactional(readOnly = true)
    public List<OrderDto> getOrdersByUser(Integer userId) {
        List<OrderDto> orders = orderJpaRepository.findByUserIdOrderByOrderDateDesc(userId)
                .stream()
                .map(OrderDto::mapToSummaryDto)
                .toList();

        return orderEnrichmentService.enrichAll(orders);
    }
}
