package com.relatos_papel.orders.application.order.create;

import com.relatos_papel.orders.application.order.common.OrderDto;
import com.relatos_papel.orders.application.order.common.SaveOrderDto;
import com.relatos_papel.orders.common.mediator.RequestHandler;
import com.relatos_papel.orders.domain.model.Order;
import com.relatos_papel.orders.domain.model.OrderItem;
import com.relatos_papel.orders.infrastructure.feign.BookSummaryDto;
import com.relatos_papel.orders.infrastructure.feign.CatalogueClient;
import com.relatos_papel.orders.infrastructure.repositories.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class CreateOrderCommandHandler implements RequestHandler<CreateOrderCommand, OrderDto> {

    private final OrderRepository orderRepository;
    private final CatalogueClient catalogueClient; // llamada HTTP a catalogue-service via Eureka

    @Override
    public OrderDto handle(CreateOrderCommand request) {
        SaveOrderDto dto = request.getData();

        // ── VALIDACIÓN CONTRA CATALOGUE-SERVICE ──────────────────────────────
        // Traemos todos los libros del catálogo y construimos un mapa bookId → libro
        List<BookSummaryDto> allBooks = catalogueClient.getAllBooks();
        Map<Long, BookSummaryDto> bookMap = allBooks.stream()
                .collect(Collectors.toMap(BookSummaryDto::getBookId, b -> b));

        for (SaveOrderDto.OrderItemInput input : dto.getItems()) {
            BookSummaryDto book = bookMap.get(input.getBookId());

            // 1. El libro debe existir en el catálogo
            if (book == null) {
                throw new RuntimeException(
                        "El libro con ID " + input.getBookId() + " no existe en el catálogo.");
            }

            // 2. El libro no debe estar oculto (isAvailable = false)
            if (Boolean.FALSE.equals(book.getIsAvailable())) {
                throw new RuntimeException(
                        "El libro '" + book.getTitle() + "' no está disponible.");
            }

            // 3. Debe haber stock suficiente
            if (book.getStock() == null || book.getStock() < input.getQuantity()) {
                throw new RuntimeException(
                        "Stock insuficiente para '" + book.getTitle() + "'. "
                        + "Disponible: " + (book.getStock() != null ? book.getStock() : 0)
                        + ", solicitado: " + input.getQuantity());
            }
        }
        // ─────────────────────────────────────────────────────────────────────

        Order order = new Order();
        order.setUserId(dto.getUserId());
        order.setAddress(dto.getAddress());
        order.setCity(dto.getCity());
        order.setCountry(dto.getCountry());
        order.setPhone(dto.getPhone());
        order.setStatusId(1); // pending — siempre arranca en estado 1
        order.setOrderDate(LocalDateTime.now());

        List<OrderItem> items = dto.getItems().stream().map(input -> {
            OrderItem item = new OrderItem();
            item.setOrder(order);
            item.setBookId(input.getBookId());
            item.setQuantity(input.getQuantity());
            item.setPrice(input.getPrice());
            return item;
        }).toList();

        order.setItems(items);

        BigDecimal total = items.stream()
                .map(i -> i.getPrice().multiply(BigDecimal.valueOf(i.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        order.setTotal(total);

        Order saved = orderRepository.save(order);
        return OrderDto.mapToDto(saved);
    }

    @Override
    public Class<CreateOrderCommand> getRequestType() {
        return CreateOrderCommand.class;
    }
}
