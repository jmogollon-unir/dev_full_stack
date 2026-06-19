package com.relatos_papel.catalogue.service;

import com.relatos_papel.catalogue.controller.model.BookDto;
import com.relatos_papel.catalogue.controller.model.SaveBookDto;
import com.relatos_papel.catalogue.exception.DuplicateResourceException;
import com.relatos_papel.catalogue.exception.ResourceNotFoundException;
import com.relatos_papel.catalogue.repository.BookJpaRepository;
import com.relatos_papel.catalogue.repository.GenreJpaRepository;
import com.relatos_papel.catalogue.repository.model.Book;
import com.relatos_papel.catalogue.repository.model.Genre;
import com.relatos_papel.catalogue.repository.model.enums.BookFormat;
import com.relatos_papel.catalogue.search.service.BookSearchIndexer;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class CreateBooksService {

    private final BookJpaRepository bookJpaRepository;
    private final GenreJpaRepository genreJpaRepository;
    private final BookSearchIndexer bookSearchIndexer;

    @Transactional
    public BookDto createBook(SaveBookDto request) {
        Genre genre = genreJpaRepository.findById(request.getGenreId())
                .orElseThrow(() -> new ResourceNotFoundException("Género no encontrado"));

        if (bookJpaRepository.existsByIsbn(request.getIsbn())) {
            throw new DuplicateResourceException("Ya existe un libro con el ISBN " + request.getIsbn());
        }

        Book book = new Book();
        book.setTitle(request.getTitle());
        book.setAuthor(request.getAuthor());
        book.setIsbn(request.getIsbn());
        book.setPrice(request.getPrice());
        book.setStock(request.getStock() != null ? request.getStock() : 0);
        book.setCoverUrl(request.getCoverUrl());
        book.setDescription(request.getDescription());
        book.setGenre(genre);
        book.setFormat(request.getFormat() != null ? BookFormat.fromValue(request.getFormat()) : BookFormat.AMBOS);
        book.setLanguage(request.getLanguage() != null ? request.getLanguage() : "Español");
        book.setPublicationDate(request.getPublicationDate());
        book.setIsAvailable(request.getIsAvailable() != null ? request.getIsAvailable() : true);

        Book savedBook = bookJpaRepository.save(book);
        bookSearchIndexer.save(savedBook);
        return BookDto.mapToDto(savedBook);
    }
}
