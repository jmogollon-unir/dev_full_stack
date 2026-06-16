package com.relatos_papel.catalogue.service;

import com.relatos_papel.catalogue.controller.model.BookDto;
import com.relatos_papel.catalogue.controller.model.SaveBookDto;
import com.relatos_papel.catalogue.exception.ResourceNotFoundException;
import com.relatos_papel.catalogue.repository.BookJpaRepository;
import com.relatos_papel.catalogue.repository.GenreJpaRepository;
import com.relatos_papel.catalogue.repository.model.Book;
import com.relatos_papel.catalogue.repository.model.enums.BookFormat;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class UpdateBooksService {

    private final BookJpaRepository bookJpaRepository;
    private final GenreJpaRepository genreJpaRepository;

    @Transactional
    public BookDto updateBook(Integer bookId, SaveBookDto request) {
        Book book = bookJpaRepository.findById(bookId)
                .orElseThrow(() -> new ResourceNotFoundException("Libro no encontrado"));

        if (request.getTitle() != null) book.setTitle(request.getTitle());
        if (request.getAuthor() != null) book.setAuthor(request.getAuthor());
        if (request.getIsbn() != null) book.setIsbn(request.getIsbn());
        if (request.getPrice() != null) book.setPrice(request.getPrice());
        if (request.getStock() != null) book.setStock(request.getStock());
        if (request.getCoverUrl() != null) book.setCoverUrl(request.getCoverUrl());
        if (request.getDescription() != null) book.setDescription(request.getDescription());
        if (request.getFormat() != null) book.setFormat(BookFormat.fromValue(request.getFormat()));
        if (request.getLanguage() != null) book.setLanguage(request.getLanguage());
        if (request.getPublicationDate() != null) book.setPublicationDate(request.getPublicationDate());
        if (request.getIsAvailable() != null) book.setIsAvailable(request.getIsAvailable());

        if (request.getGenreId() != null) {
            book.setGenre(genreJpaRepository.findById(request.getGenreId())
                    .orElseThrow(() -> new ResourceNotFoundException("Género no encontrado")));
        }

        Book savedBook = bookJpaRepository.save(book);
        return BookDto.mapToDto(savedBook);
    }

    @Transactional
    public BookDto replaceBook(Integer bookId, SaveBookDto request) {
        Book book = bookJpaRepository.findById(bookId)
                .orElseThrow(() -> new ResourceNotFoundException("Libro no encontrado"));

        book.setTitle(request.getTitle());
        book.setAuthor(request.getAuthor());
        book.setIsbn(request.getIsbn());
        book.setPrice(request.getPrice());
        book.setStock(request.getStock());
        book.setCoverUrl(request.getCoverUrl());
        book.setDescription(request.getDescription());
        book.setFormat(request.getFormat() != null ? BookFormat.fromValue(request.getFormat()) : BookFormat.AMBOS);
        book.setLanguage(request.getLanguage() != null ? request.getLanguage() : "Español");
        book.setPublicationDate(request.getPublicationDate());
        book.setIsAvailable(request.getIsAvailable() != null ? request.getIsAvailable() : true);

        if (request.getGenreId() != null) {
            book.setGenre(genreJpaRepository.findById(request.getGenreId())
                    .orElseThrow(() -> new ResourceNotFoundException("Género no encontrado")));
        }

        Book savedBook = bookJpaRepository.save(book);
        return BookDto.mapToDto(savedBook);
    }
}
