package com.relatos_papel.orders.controller;

import com.relatos_papel.orders.controller.model.CreateOrderRequestDto;
import com.relatos_papel.orders.controller.model.OrderDto;
import com.relatos_papel.orders.security.AccessTokenUserIdExtractor;
import com.relatos_papel.orders.service.CreateOrdersService;
import com.relatos_papel.orders.service.GetOrdersService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
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
    private final AccessTokenUserIdExtractor accessTokenUserIdExtractor;

    @Value("${app.security.require-access-token:true}")
    private boolean requireAccessToken;

    @PostMapping // TODO: Manejar error cuando no hay stock
    public ResponseEntity<OrderDto> createOrder(
            @RequestHeader(value = "accessToken", required = false) String accessToken,
            @RequestBody CreateOrderRequestDto request) {
        if (requireAccessToken) {
            Integer uid = accessTokenUserIdExtractor.extractUserId(accessToken);
            if (uid == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            }
            if (request.getUserId() == null || !request.getUserId().equals(uid)) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
            }
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(createOrdersService.createOrder(request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderDto> getOrderById(
            @RequestHeader(value = "accessToken", required = false) String accessToken,
            @PathVariable Integer id) {
        OrderDto dto = getOrdersService.getOrderById(id);
        if (requireAccessToken) {
            Integer uid = accessTokenUserIdExtractor.extractUserId(accessToken);
            if (uid == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            }
            if (dto.getUserId() == null || !dto.getUserId().equals(uid)) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
            }
        }
        return ResponseEntity.ok(dto);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<OrderDto>> getOrdersByUser(
            @RequestHeader(value = "accessToken", required = false) String accessToken,
            @PathVariable Integer userId) {
        if (requireAccessToken) {
            Integer uid = accessTokenUserIdExtractor.extractUserId(accessToken);
            if (uid == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            }
            if (!userId.equals(uid)) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
            }
        }
        return ResponseEntity.ok(getOrdersService.getOrdersByUser(userId));
    }
}
