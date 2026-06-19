package com.relatos_papel.catalogue.repository;

import com.relatos_papel.catalogue.repository.model.Book;
import org.jspecify.annotations.NonNull;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BookJpaRepository extends JpaRepository<Book, Integer>, JpaSpecificationExecutor<Book> {

    @EntityGraph(attributePaths = "genre")
    Optional<Book> findById(@NonNull Integer bookId);

    @Override
    @NonNull
    @EntityGraph(attributePaths = "genre")
    List<Book> findAll();

    boolean existsByIsbn(String isbn);
}
