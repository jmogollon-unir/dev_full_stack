package com.relatos_papel.orders.service;

import com.relatos_papel.orders.controller.model.CreateOrderRequestDto;
import com.relatos_papel.orders.controller.model.OrderDto;
import com.relatos_papel.orders.event.service.OrderEventService;
import com.relatos_papel.orders.exception.OrderValidationException;
import com.relatos_papel.orders.exception.ResourceNotFoundException;
import com.relatos_papel.orders.facade.BooksCatalogueFacade;
import com.relatos_papel.orders.facade.model.BookSummaryDto;
import com.relatos_papel.orders.repository.AddressRepository;
import com.relatos_papel.orders.repository.OrderJpaRepository;
import com.relatos_papel.orders.repository.UserRepository;
import com.relatos_papel.orders.repository.model.Address;
import com.relatos_papel.orders.repository.model.Order;
import com.relatos_papel.orders.repository.model.OrderItem;
import com.relatos_papel.orders.repository.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class CreateOrdersService {

    private final OrderJpaRepository orderJpaRepository;
    private final UserRepository userRepository;
    private final AddressRepository addressRepository;
    private final BooksCatalogueFacade booksCatalogueFacade;
    private final OrderEnrichmentService orderEnrichmentService;
    private final OrderEventService orderEventService;

    @Transactional
    public OrderDto createOrder(CreateOrderRequestDto request) {

        if (request.getUserId() == null) {
            throw new OrderValidationException("El userId es obligatorio.");
        }

        if (request.getItems() == null || request.getItems().isEmpty()) {
            throw new OrderValidationException("El pedido debe contener al menos un libro.");
        }

        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("No se encontró el usuario con ID " + request.getUserId()));

        Address address = addressRepository.findByUserIdAndIsDefaultTrue(request.getUserId())
                .or(() -> addressRepository.findFirstByUserIdOrderByAddressIdAsc(request.getUserId()))
                .orElseThrow(() -> new ResourceNotFoundException("No se encontró una dirección para el usuario con ID " + request.getUserId()));

        List<OrderItem> items = new ArrayList<>();

        for (CreateOrderRequestDto.RequestedBook input : request.getItems()) {
            BookSummaryDto book = booksCatalogueFacade.getBook(input.getBookId());

            if (Boolean.FALSE.equals(book.getIsAvailable())) {
                throw new OrderValidationException("El libro '" + book.getTitle() + "' no está disponible para la venta.");
            }

            if (book.getStock() == null || book.getStock() < input.getQuantity()) {
                throw new OrderValidationException(
                        "Stock insuficiente para '" + book.getTitle() + "'. Disponible: "
                                + (book.getStock() != null ? book.getStock() : 0) + ", solicitado: " + input.getQuantity());
            }

            OrderItem item = new OrderItem();
            item.setBookId(input.getBookId());
            item.setQuantity(input.getQuantity());
            item.setPrice(book.getPrice());
            items.add(item);
        }

        Order order = new Order();
        order.setUserId(request.getUserId());
        order.setAddress(address.getAddress());
        order.setCity(address.getCity());
        order.setCountry(address.getCountry());
        order.setPhone(address.getPhone());
        order.setStatusId(1);
        order.setOrderDate(LocalDateTime.now());

        for (OrderItem item : items) {
            item.setOrder(order);
        }
        order.setOrderItems(items);

        BigDecimal total = items.stream()
                .map(i -> i.getPrice().multiply(BigDecimal.valueOf(i.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        order.setTotal(total);

        Order savedOrder = orderJpaRepository.save(order);

        for (CreateOrderRequestDto.RequestedBook input : request.getItems()) {
            booksCatalogueFacade.decreaseBookStock(input.getBookId(), input.getQuantity());
        }

        orderEventService.publishOrderCreatedEvent(savedOrder, user.getEmail());

        OrderDto result = OrderDto.mapToDto(savedOrder);
        return orderEnrichmentService.enrich(result);
    }
}
