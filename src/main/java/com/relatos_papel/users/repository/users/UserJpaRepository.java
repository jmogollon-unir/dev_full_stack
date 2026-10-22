package com.relatos_papel.users.repository.users;

import com.relatos_papel.users.repository.users.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserJpaRepository extends JpaRepository<User, Integer> {

    Optional<User> findByCif(String cif);

    Optional<User> findByEmail(String email);

    List<User> findByNameContainingIgnoreCase(String name);
}
