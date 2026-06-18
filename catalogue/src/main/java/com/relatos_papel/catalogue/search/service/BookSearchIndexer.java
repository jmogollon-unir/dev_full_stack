package com.relatos_papel.catalogue.search.service;

import com.relatos_papel.catalogue.repository.BookJpaRepository;
import com.relatos_papel.catalogue.repository.model.Book;
import com.relatos_papel.catalogue.search.model.BookSearchDocument;
import com.relatos_papel.catalogue.search.repository.BookSearchRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class BookSearchIndexer {

    private final BookSearchRepository bookSearchRepository;
    private final BookJpaRepository bookJpaRepository;

    @Value("${catalogue.elasticsearch.reindex-on-startup:true}")
    private boolean reindexOnStartup;

    public void save(Book book) {
        bookSearchRepository.save(BookSearchDocument.fromBook(book));
    }

    public void deleteById(Integer bookId) {
        bookSearchRepository.deleteById(bookId);
    }

    @EventListener(ApplicationReadyEvent.class)
    @Transactional(readOnly = true)
    public void reindexOnStartup() {
        if (!reindexOnStartup) {
            return;
        }

        Iterable<BookSearchDocument> documents = bookJpaRepository.findAll().stream()
                .map(BookSearchDocument::fromBook)
                .toList();
        bookSearchRepository.saveAll(documents);
        log.info("Indice Elasticsearch books sincronizado con MySQL al arrancar catalogue");
    }
}
