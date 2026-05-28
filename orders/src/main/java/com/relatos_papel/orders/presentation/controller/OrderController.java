package com.relatos_papel.orders.presentation.controller;

import com.relatos_papel.orders.application.order.common.OrderDto;
import com.relatos_papel.orders.application.order.common.SaveOrderDto;
import com.relatos_papel.orders.application.order.create.CreateOrderCommand;
import com.relatos_papel.orders.application.order.find.GetOrderByIdQuery;
import com.relatos_papel.orders.application.order.find.GetOrdersByUserQuery;
import com.relatos_papel.orders.common.mediator.Mediator;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {

    private final Mediator mediator;

    // 1. Crear pedido
    @PostMapping
    public ResponseEntity<OrderDto> createOrder(@RequestBody SaveOrderDto dto) {
        var command = new CreateOrderCommand(dto);
        var result = mediator.dispatch(command);
        return new ResponseEntity<>(result, HttpStatus.CREATED);
    }

    // 2. Obtener pedido por ID (incluye títulos de libros via Feign)
    @GetMapping("/{id}")
    public ResponseEntity<OrderDto> getOrderById(@PathVariable Long id) {
        var query = new GetOrderByIdQuery(id);
        var result = mediator.dispatch(query);
        return ResponseEntity.ok(result);
    }

    // 3. Obtener todos los pedidos de un usuario
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<OrderDto>> getOrdersByUser(@PathVariable Long userId) {
        var query = new GetOrdersByUserQuery(userId);
        var result = mediator.dispatch(query);
        return ResponseEntity.ok(result);
    }
}
