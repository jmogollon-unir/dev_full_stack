package com.relatos_papel.catalogue.application.book.search;

import com.relatos_papel.catalogue.application.book.common.BookDto;
import com.relatos_papel.catalogue.common.mediator.RequestHandler;
import com.relatos_papel.catalogue.domain.model.Book;
import com.relatos_papel.catalogue.infrastructure.repositories.BookRepository;
import jakarta.persistence.criteria.Predicate;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class SearchBooksQueryHandler implements RequestHandler<SearchBooksQuery, List<BookDto>> {

    private final BookRepository bookRepository;

    @Override
    public List<BookDto> handle(SearchBooksQuery request) {
        // Especificación dinámica para Spring Data JPA
        Specification<Book> spec = (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            // REGLA DE NEGOCIO: Nunca devolver libros ocultos
            predicates.add(cb.isTrue(root.get("isAvailable")));

            if (request.getTitle() != null)
                predicates.add(cb.like(cb.lower(root.get("title")), "%" + request.getTitle().toLowerCase() + "%"));

            if (request.getAuthor() != null)
                predicates.add(cb.like(cb.lower(root.get("author")), "%" + request.getAuthor().toLowerCase() + "%"));

            if (request.getIsbn() != null)
                predicates.add(cb.equal(root.get("isbn"), request.getIsbn()));

            if (request.getPopularity() != null)
                predicates.add(cb.equal(root.get("popularity"), request.getPopularity()));

            if (request.getCategory() != null) {
                // Join con la tabla Genre para buscar por nombre de categoría
                predicates.add(cb.like(cb.lower(root.get("genre").get("name")), "%" + request.getCategory().toLowerCase() + "%"));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };

        // Ejecutar búsqueda y mapear a DTO
        return bookRepository.findAll(spec).stream()
                .map(BookDto::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    public Class<SearchBooksQuery> getRequestType() {
        return SearchBooksQuery.class;
    }
}