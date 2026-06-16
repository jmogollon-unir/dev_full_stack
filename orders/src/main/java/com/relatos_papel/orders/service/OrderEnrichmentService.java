package com.relatos_papel.orders.service;

import com.relatos_papel.orders.controller.model.OrderDto;
import com.relatos_papel.orders.exception.ResourceNotFoundException;
import com.relatos_papel.orders.facade.BooksCatalogueFacade;
import com.relatos_papel.orders.facade.model.BookSummaryDto;
import com.relatos_papel.orders.repository.OrderStatusRepository;
import com.relatos_papel.orders.repository.UserRepository;
import com.relatos_papel.orders.repository.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderEnrichmentService {

    private final BooksCatalogueFacade booksCatalogueFacade;
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
                .orElseThrow(() -> new ResourceNotFoundException("No se encontró el usuario con ID " + dto.getUserId()));
        dto.setEmail(user.getEmail());
        dto.setUsername(user.getUsername());

        orderStatusRepository.findById(dto.getStatusId())
                .ifPresent(status -> dto.setStatusName(status.getStatusName()));
    }

    private void enrichBookTitles(OrderDto dto) {
        enrichBookTitles(dto, fetchBookTitles());
    }

    private void enrichBookTitles(OrderDto dto, Map<Integer, String> bookTitles) {
        dto.getItems().forEach(item ->
                item.setBookTitle(bookTitles.getOrDefault(item.getBookId(), "Libro no encontrado")));
    }

    private Map<Integer, String> fetchBookTitles() {
        return booksCatalogueFacade.getAllBooks().stream()
                .collect(Collectors.toMap(BookSummaryDto::getBookId, BookSummaryDto::getTitle, (a, b) -> a));
    }
}
