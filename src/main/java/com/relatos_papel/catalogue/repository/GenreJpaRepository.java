package com.relatos_papel.catalogue.repository;

import com.relatos_papel.catalogue.repository.model.Genre;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GenreJpaRepository extends JpaRepository<Genre, Integer> {
}
