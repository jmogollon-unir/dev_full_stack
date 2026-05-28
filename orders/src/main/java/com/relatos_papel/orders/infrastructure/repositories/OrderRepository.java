package com.relatos_papel.orders.infrastructure.repositories;

import com.relatos_papel.orders.domain.model.Order;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order, Integer> {

    @EntityGraph(attributePaths = "items")
    Optional<Order> findById(Integer orderId);

    @EntityGraph(attributePaths = "items")
    List<Order> findByUserIdOrderByOrderDateDesc(Integer userId);
}
