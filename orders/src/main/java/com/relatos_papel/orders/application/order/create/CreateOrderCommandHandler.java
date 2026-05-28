package com.relatos_papel.orders.application.order.create;

import com.relatos_papel.orders.application.order.common.OrderDto;
import com.relatos_papel.orders.application.order.common.OrderEnrichmentService;
import com.relatos_papel.orders.application.order.common.SaveOrderDto;
import com.relatos_papel.orders.common.exception.OrderValidationException;
import com.relatos_papel.orders.common.exception.ResourceNotFoundException;
import com.relatos_papel.orders.common.mediator.RequestHandler;
import com.relatos_papel.orders.domain.model.Address;
import com.relatos_papel.orders.domain.model.Order;
import com.relatos_papel.orders.domain.model.OrderItem;
import com.relatos_papel.orders.infrastructure.feign.BookSummaryDto;
import com.relatos_papel.orders.infrastructure.feign.CatalogueClient;
import com.relatos_papel.orders.infrastructure.feign.StockDecreaseDto;
import com.relatos_papel.orders.infrastructure.repositories.AddressRepository;
import com.relatos_papel.orders.infrastructure.repositories.OrderRepository;
import com.relatos_papel.orders.infrastructure.repositories.UserRepository;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class CreateOrderCommandHandler implements RequestHandler<CreateOrderCommand, OrderDto> {

    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final AddressRepository addressRepository;
    private final CatalogueClient catalogueClient;
    private final OrderEnrichmentService orderEnrichmentService;

    @Override
    @Transactional
    public OrderDto handle(CreateOrderCommand request) {
        SaveOrderDto dto = request.getData();

        if (dto.getUserId() == null) {
            throw new OrderValidationException("El userId es obligatorio.");
        }

        if (dto.getItems() == null || dto.getItems().isEmpty()) {
            throw new OrderValidationException("El pedido debe contener al menos un libro.");
        }

        userRepository.findById(dto.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("No se encontró el usuario con ID " + dto.getUserId()));

        Address address = addressRepository.findByUserIdAndIsDefaultTrue(dto.getUserId())
                .or(() -> addressRepository.findFirstByUserIdOrderByAddressIdAsc(dto.getUserId()))
                .orElseThrow(() -> new ResourceNotFoundException("No se encontró una dirección para el usuario con ID " + dto.getUserId()));

        List<OrderItem> items = new ArrayList<>();

        for (SaveOrderDto.OrderItemInput input : dto.getItems()) {
            BookSummaryDto book = fetchBook(input.getBookId());

            if (Boolean.FALSE.equals(book.getIsAvailable())) {
                throw new OrderValidationException(
                        "El libro '" + book.getTitle() + "' no está disponible para la venta.");
            }

            if (book.getStock() == null || book.getStock() < input.getQuantity()) {
                throw new OrderValidationException(
                        "Stock insuficiente para '" + book.getTitle() + "'. Disponible: "
                                + (book.getStock() != null ? book.getStock() : 0)
                                + ", solicitado: " + input.getQuantity());
            }

            OrderItem item = new OrderItem();
            item.setBookId(input.getBookId());
            item.setQuantity(input.getQuantity());
            item.setPrice(book.getPrice());
            items.add(item);
        }

        Order order = new Order();
        order.setUserId(dto.getUserId());
        order.setAddress(address.getAddress());
        order.setCity(address.getCity());
        order.setCountry(address.getCountry());
        order.setPhone(address.getPhone());
        order.setStatusId(1);
        order.setOrderDate(LocalDateTime.now());

        for (OrderItem item : items) {
            item.setOrder(order);
        }
        order.setItems(items);

        BigDecimal total = items.stream()
                .map(i -> i.getPrice().multiply(BigDecimal.valueOf(i.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        order.setTotal(total);

        Order saved = orderRepository.save(order);

        for (SaveOrderDto.OrderItemInput input : dto.getItems()) {
            decreaseBookStock(input.getBookId(), input.getQuantity());
        }

        OrderDto result = OrderDto.mapToDto(saved);
        return orderEnrichmentService.enrich(result);
    }

    private void decreaseBookStock(Integer bookId, Integer quantity) {
        try {
            StockDecreaseDto dto = new StockDecreaseDto();
            dto.setQuantity(quantity);
            catalogueClient.decreaseBookStock(bookId, dto);
        } catch (FeignException.BadRequest e) {
            throw new OrderValidationException("No se pudo actualizar el stock del libro con ID " + bookId + ".");
        } catch (FeignException.NotFound e) {
            throw new OrderValidationException("El libro con ID " + bookId + " no existe en el catálogo.");
        } catch (FeignException e) {
            throw new OrderValidationException("No se pudo actualizar el stock del libro con ID " + bookId
                    + " en el microservicio catalogue.");
        }
    }

    private BookSummaryDto fetchBook(Integer bookId) {
        try {
            return catalogueClient.getBookById(bookId);
        } catch (FeignException.NotFound e) {
            throw new OrderValidationException("El libro con ID " + bookId + " no existe en el catálogo.");
        } catch (FeignException e) {
            throw new OrderValidationException("No se pudo validar el libro con ID " + bookId
                    + " en el microservicio catalogue.");
        }
    }

    @Override
    public Class<CreateOrderCommand> getRequestType() {
        return CreateOrderCommand.class;
    }
}
