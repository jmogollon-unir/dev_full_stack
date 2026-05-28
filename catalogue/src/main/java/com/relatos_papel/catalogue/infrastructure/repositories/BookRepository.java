package com.relatos_papel.catalogue.infrastructure.repositories;

import com.relatos_papel.catalogue.domain.model.Book;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BookRepository extends JpaRepository<Book, Integer>, JpaSpecificationExecutor<Book> {

    @EntityGraph(attributePaths = "genre")
    Optional<Book> findById(Integer bookId);

    boolean existsByIsbn(String isbn);
}