package com.relatos_papel.catalogue.infrastructure.repositories;

import com.relatos_papel.catalogue.domain.model.Genre;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GenreRepository extends JpaRepository<Genre, Long> {
}