package com.relatos_papel.orders.application.order.common;

import com.relatos_papel.orders.common.exception.ResourceNotFoundException;
import com.relatos_papel.orders.domain.model.User;
import com.relatos_papel.orders.infrastructure.feign.BookSummaryDto;
import com.relatos_papel.orders.infrastructure.feign.CatalogueClient;
import com.relatos_papel.orders.infrastructure.repositories.OrderStatusRepository;
import com.relatos_papel.orders.infrastructure.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class OrderEnrichmentService {

    private final CatalogueClient catalogueClient;
    private final UserRepository userRepository;
    private final OrderStatusRepository orderStatusRepository;

    public OrderDto enrich(OrderDto dto) {
        enrichUserAndStatus(dto);
        enrichBookTitles(dto);
        return dto;
    }

    public List<OrderDto> enrichAll(List<OrderDto> orders) {
        Map<Integer, String> bookTitles = fetchBookTitles();
        return orders.stream().map(order -> {
            enrichUserAndStatus(order);
            enrichBookTitles(order, bookTitles);
            return order;
        }).toList();
    }

    private void enrichUserAndStatus(OrderDto dto) {
        User user = userRepository.findById(dto.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "No se encontró el usuario con ID " + dto.getUserId()));
        dto.setEmail(user.getEmail());
        dto.setUsername(user.getUsername());

        orderStatusRepository.findById(dto.getStatusId())
                .ifPresent(status -> dto.setStatusName(status.getStatusName()));
    }

    private void enrichBookTitles(OrderDto dto) {
        enrichBookTitles(dto, fetchBookTitles());
    }

    private void enrichBookTitles(OrderDto dto, Map<Integer, String> bookTitles) {
        List<OrderItemDto> enrichedItems = dto.getItems().stream().map(item -> {
            item.setBookTitle(bookTitles.getOrDefault(item.getBookId(), "Libro no encontrado"));
            return item;
        }).toList();
        dto.setItems(enrichedItems);
    }

    private Map<Integer, String> fetchBookTitles() {
        try {
            return catalogueClient.getAllBooks().stream()
                    .collect(Collectors.toMap(BookSummaryDto::getBookId, BookSummaryDto::getTitle, (a, b) -> a));
        } catch (Exception e) {
            return Map.of();
        }
    }
}
