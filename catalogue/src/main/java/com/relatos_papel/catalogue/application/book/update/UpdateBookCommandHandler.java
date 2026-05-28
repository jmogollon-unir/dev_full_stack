package com.relatos_papel.catalogue.application.book.update;

import com.relatos_papel.catalogue.application.book.common.BookDto;
import com.relatos_papel.catalogue.common.exception.ResourceNotFoundException;
import com.relatos_papel.catalogue.common.mediator.RequestHandler;
import com.relatos_papel.catalogue.domain.model.Book;
import com.relatos_papel.catalogue.domain.model.enums.BookFormat;
import com.relatos_papel.catalogue.infrastructure.repositories.BookRepository;
import com.relatos_papel.catalogue.infrastructure.repositories.GenreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UpdateBookCommandHandler implements RequestHandler<UpdateBookCommand, BookDto> {

    private final BookRepository bookRepository;
    private final GenreRepository genreRepository;

    @Override
    public BookDto handle(UpdateBookCommand request) {
        Book book = bookRepository.findById(request.getBookId())
                .orElseThrow(() -> new ResourceNotFoundException("Libro no encontrado"));

        var data = request.getData();
        if (data.getTitle() != null) book.setTitle(data.getTitle());
        if (data.getAuthor() != null) book.setAuthor(data.getAuthor());
        if (data.getIsbn() != null) book.setIsbn(data.getIsbn());
        if (data.getPrice() != null) book.setPrice(data.getPrice());
        if (data.getStock() != null) book.setStock(data.getStock());
        if (data.getCoverUrl() != null) book.setCoverUrl(data.getCoverUrl());
        if (data.getDescription() != null) book.setDescription(data.getDescription());
        if (data.getFormat() != null) book.setFormat(BookFormat.fromValue(data.getFormat()));
        if (data.getLanguage() != null) book.setLanguage(data.getLanguage());
        if (data.getPublicationDate() != null) book.setPublicationDate(data.getPublicationDate());
        if (data.getIsAvailable() != null) book.setIsAvailable(data.getIsAvailable());

        if (data.getGenreId() != null) {
            book.setGenre(genreRepository.findById(data.getGenreId())
                    .orElseThrow(() -> new ResourceNotFoundException("Género no encontrado")));
        }

        Book savedBook = bookRepository.save(book);
        return BookDto.mapToDto(savedBook);
    }

    @Override
    public Class<UpdateBookCommand> getRequestType() {
        return UpdateBookCommand.class;
    }
}
