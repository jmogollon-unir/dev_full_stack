package com.relatos_papel.catalogue.search.repository;

import com.relatos_papel.catalogue.search.model.BookSearchDocument;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookSearchRepository extends ElasticsearchRepository<BookSearchDocument, Integer> {
}
