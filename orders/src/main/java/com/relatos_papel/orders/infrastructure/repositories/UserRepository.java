package com.relatos_papel.orders.infrastructure.repositories;

import com.relatos_papel.orders.domain.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
}
