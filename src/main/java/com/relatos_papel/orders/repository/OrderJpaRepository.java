package com.relatos_papel.orders.repository;

import com.relatos_papel.orders.repository.model.Order;
import org.jspecify.annotations.NonNull;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrderJpaRepository extends JpaRepository<Order, Integer> {

    @EntityGraph(attributePaths = "orderItems")
    Optional<Order> findById(@NonNull Integer orderId);

    @EntityGraph(attributePaths = "orderItems")
    List<Order> findByUserIdOrderByOrderDateDesc(Integer userId);
}
