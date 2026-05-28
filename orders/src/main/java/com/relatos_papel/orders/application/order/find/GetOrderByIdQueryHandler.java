package com.relatos_papel.orders.application.order.find;

import com.relatos_papel.orders.application.order.common.OrderDto;
import com.relatos_papel.orders.application.order.common.OrderItemDto;
import com.relatos_papel.orders.common.mediator.RequestHandler;
import com.relatos_papel.orders.infrastructure.feign.BookSummaryDto;
import com.relatos_papel.orders.infrastructure.feign.CatalogueClient;
import com.relatos_papel.orders.infrastructure.repositories.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class GetOrderByIdQueryHandler implements RequestHandler<GetOrderByIdQuery, OrderDto> {

    private final OrderRepository orderRepository;
    private final CatalogueClient catalogueClient;

    @Override
    public OrderDto handle(GetOrderByIdQuery request) {
        var order = orderRepository.findById(request.getOrderId())
                .orElseThrow(() -> new RuntimeException("Pedido no encontrado: " + request.getOrderId()));

        // Enriquecer ítems con el título del libro desde catalogue
        Map<Long, String> bookTitles = fetchBookTitles();

        OrderDto dto = OrderDto.mapToDto(order);
        List<OrderItemDto> enrichedItems = dto.getItems().stream().map(item -> {
            item.setBookTitle(bookTitles.getOrDefault(item.getBookId(), "Libro no encontrado"));
            return item;
        }).collect(Collectors.toList());

        dto.setItems(enrichedItems);
        return dto;
    }

    @Override
    public Class<GetOrderByIdQuery> getRequestType() {
        return GetOrderByIdQuery.class;
    }

    private Map<Long, String> fetchBookTitles() {
        try {
            return catalogueClient.getAllBooks().stream()
                    .collect(Collectors.toMap(BookSummaryDto::getBookId, BookSummaryDto::getTitle));
        } catch (Exception e) {
            // Si catalogue no está disponible, devolvemos mapa vacío (degradación elegante)
            return Map.of();
        }
    }
}
