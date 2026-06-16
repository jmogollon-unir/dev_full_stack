package com.relatos_papel.orders.controller;

import com.relatos_papel.orders.controller.model.CreateOrderRequestDto;
import com.relatos_papel.orders.controller.model.OrderDto;
import com.relatos_papel.orders.service.CreateOrdersService;
import com.relatos_papel.orders.service.GetOrdersService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrdersController {

    private final CreateOrdersService createOrdersService;
    private final GetOrdersService getOrdersService;

    @PostMapping // TODO: Manejar error cuando no hay stock
    public ResponseEntity<OrderDto> createOrder(@RequestBody CreateOrderRequestDto request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(createOrdersService.createOrder(request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderDto> getOrderById(@PathVariable Integer id) {
        return ResponseEntity.ok(getOrdersService.getOrderById(id));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<OrderDto>> getOrdersByUser(@PathVariable Integer userId) {
        return ResponseEntity.ok(getOrdersService.getOrdersByUser(userId));
    }
}
