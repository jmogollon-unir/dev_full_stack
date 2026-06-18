package com.relatos_papel.catalogue.search.service;

import com.relatos_papel.catalogue.controller.model.BookDto;
import com.relatos_papel.catalogue.controller.model.SearchBooksCriteria;
import com.relatos_papel.catalogue.exception.ResourceNotFoundException;
import com.relatos_papel.catalogue.search.model.BookSearchDocument;
import com.relatos_papel.catalogue.search.repository.BookSearchRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.query.Criteria;
import org.springframework.data.elasticsearch.core.query.CriteriaQuery;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BookSearchService {

    private final BookSearchRepository bookSearchRepository;
    private final ElasticsearchOperations elasticsearchOperations;

    public BookDto getBookById(Integer id) {
        return bookSearchRepository.findById(id)
                .map(BookSearchDocument::toDto)
                .orElseThrow(() -> new ResourceNotFoundException("No hay libros con el ID " + id));
    }

    public List<BookDto> searchBooks(SearchBooksCriteria criteria) {
        Criteria elasticCriteria = buildCriteria(criteria);
        CriteriaQuery query = new CriteriaQuery(elasticCriteria);

        return elasticsearchOperations.search(query, BookSearchDocument.class).stream()
                .map(SearchHit::getContent)
                .map(BookSearchDocument::toDto)
                .toList();
    }

    private Criteria buildCriteria(SearchBooksCriteria criteria) {
        Criteria elasticCriteria = new Criteria("isAvailable")
                .is(criteria.getVisible() != null ? criteria.getVisible() : Boolean.TRUE);

        if (criteria.getTitle() != null) {
            elasticCriteria = elasticCriteria.and(new Criteria("title").contains(criteria.getTitle()));
        }

        if (criteria.getAuthor() != null) {
            elasticCriteria = elasticCriteria.and(new Criteria("author").contains(criteria.getAuthor()));
        }

        if (criteria.getIsbn() != null) {
            elasticCriteria = elasticCriteria.and(new Criteria("isbn").is(criteria.getIsbn()));
        }

        if (criteria.getPopularity() != null) {
            elasticCriteria = elasticCriteria.and(new Criteria("popularity").is(criteria.getPopularity()));
        }

        if (criteria.getPublicationDate() != null) {
            elasticCriteria = elasticCriteria.and(new Criteria("publicationDate").is(criteria.getPublicationDate()));
        }

        if (criteria.getRating() != null) {
            elasticCriteria = elasticCriteria.and(new Criteria("popularity").is(criteria.getRating()));
        }

        if (criteria.getCategory() != null) {
            elasticCriteria = elasticCriteria.and(new Criteria("genreId").is(criteria.getCategory()));
        }

        if (criteria.getCategoryName() != null) {
            elasticCriteria = elasticCriteria.and(new Criteria("genreName").contains(criteria.getCategoryName()));
        }

        return elasticCriteria;
    }
}
