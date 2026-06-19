package com.relatos_papel.catalogue.service;

import com.relatos_papel.catalogue.controller.model.BookDto;
import com.relatos_papel.catalogue.controller.model.SearchBooksCriteria;
import com.relatos_papel.catalogue.search.service.BookSearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class GetBooksService {

    private final BookSearchService bookSearchService;

    public BookDto getBookById(Integer id) {
        return bookSearchService.getBookById(id);
    }

    public List<BookDto> searchBooks(SearchBooksCriteria criteria) {
        return bookSearchService.searchBooks(criteria);
    }
}
