package com.relatos_papel.catalogue.application.book.create;

import com.relatos_papel.catalogue.application.book.common.BookDto;
import com.relatos_papel.catalogue.common.mediator.RequestHandler;
import com.relatos_papel.catalogue.domain.model.Book;
import com.relatos_papel.catalogue.domain.model.Genre;
import com.relatos_papel.catalogue.domain.model.enums.BookFormat;
import com.relatos_papel.catalogue.infrastructure.repositories.BookRepository;
import com.relatos_papel.catalogue.infrastructure.repositories.GenreRepository;
import com.relatos_papel.catalogue.common.exception.DuplicateResourceException;
import com.relatos_papel.catalogue.common.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CreateBookCommandHandler implements RequestHandler<CreateBookCommand, BookDto> {

    private final BookRepository bookRepository;
    private final GenreRepository genreRepository;

    @Override
    public BookDto handle(CreateBookCommand request) {
        Genre genre = genreRepository.findById(request.getData().getGenreId())
                .orElseThrow(() -> new ResourceNotFoundException("Género no encontrado"));

        if (bookRepository.existsByIsbn(request.getData().getIsbn())) {
            throw new DuplicateResourceException("Ya existe un libro con el ISBN " + request.getData().getIsbn());
        }

        Book book = new Book();
        book.setTitle(request.getData().getTitle());
        book.setAuthor(request.getData().getAuthor());
        book.setIsbn(request.getData().getIsbn());
        book.setPrice(request.getData().getPrice());
        book.setStock(request.getData().getStock() != null ? request.getData().getStock() : 0);
        book.setCoverUrl(request.getData().getCoverUrl());
        book.setDescription(request.getData().getDescription());
        book.setGenre(genre);
        book.setFormat(request.getData().getFormat() != null ? BookFormat.fromValue(request.getData().getFormat()) : BookFormat.AMBOS);
        book.setLanguage(request.getData().getLanguage() != null ? request.getData().getLanguage() : "Español");
        book.setPublicationDate(request.getData().getPublicationDate());
        book.setIsAvailable(request.getData().getIsAvailable() != null ? request.getData().getIsAvailable() : true);

        Book savedBook = bookRepository.save(book);
        return BookDto.mapToDto(savedBook);
    }

    @Override
    public Class<CreateBookCommand> getRequestType() {
        return CreateBookCommand.class;
    }
}
