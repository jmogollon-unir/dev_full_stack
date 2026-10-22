package com.relatos_papel.orders.repository;

import com.relatos_papel.orders.repository.model.Address;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AddressRepository extends JpaRepository<Address, Integer> {

    Optional<Address> findByUserIdAndIsDefaultTrue(Integer userId);

    Optional<Address> findFirstByUserIdOrderByAddressIdAsc(Integer userId);
}
