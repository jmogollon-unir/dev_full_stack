package com.relatos_papel.catalogue.service;

import com.relatos_papel.catalogue.controller.model.BookDto;
import com.relatos_papel.catalogue.controller.model.SearchBooksCriteria;
import com.relatos_papel.catalogue.exception.ResourceNotFoundException;
import com.relatos_papel.catalogue.repository.BookJpaRepository;
import com.relatos_papel.catalogue.repository.model.Book;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class GetBooksService {

    private final BookJpaRepository bookJpaRepository;

    @Transactional(readOnly = true)
    public BookDto getBookById(Integer id) {
        Book book = bookJpaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No hay libros con el ID " + id));
        return BookDto.mapToDto(book);
    }

    @Transactional(readOnly = true)
    public List<BookDto> searchBooks(SearchBooksCriteria criteria) {
        Specification<Book> spec = (root, query, cb) -> {
            root.fetch("genre", JoinType.LEFT);
            query.distinct(true);

            List<Predicate> predicates = new ArrayList<>();

            if (criteria.getVisible() != null) {
                predicates.add(cb.equal(root.get("isAvailable"), criteria.getVisible()));
            } else {
                predicates.add(cb.isTrue(root.get("isAvailable")));
            }

            if (criteria.getTitle() != null) {
                predicates.add(cb.like(cb.lower(root.get("title")), "%" + criteria.getTitle().toLowerCase() + "%"));
            }

            if (criteria.getAuthor() != null) {
                predicates.add(cb.like(cb.lower(root.get("author")), "%" + criteria.getAuthor().toLowerCase() + "%"));
            }

            if (criteria.getIsbn() != null) {
                predicates.add(cb.equal(root.get("isbn"), criteria.getIsbn()));
            }

            if (criteria.getPopularity() != null) {
                predicates.add(cb.equal(root.get("popularity"), criteria.getPopularity()));
            }

            if (criteria.getPublicationDate() != null) {
                predicates.add(cb.equal(root.get("publicationDate"), criteria.getPublicationDate()));
            }

            if (criteria.getRating() != null) {
                predicates.add(cb.equal(root.get("popularity"), criteria.getRating()));
            }

            if (criteria.getCategory() != null) {
                predicates.add(cb.equal(root.get("genre").get("genreId"), criteria.getCategory()));
            }

            if (criteria.getCategoryName() != null) {
                predicates.add(cb.like(cb.lower(root.get("genre").get("name")), "%" + criteria.getCategoryName().toLowerCase() + "%"));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };

        return bookJpaRepository.findAll(spec).stream()
                .map(BookDto::mapToDto)
                .toList();
    }
}
