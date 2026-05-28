package com.relatos_papel.catalogue.application.book.replace;

import com.relatos_papel.catalogue.application.book.common.BookDto;
import com.relatos_papel.catalogue.common.mediator.RequestHandler;
import com.relatos_papel.catalogue.domain.model.Book;
import com.relatos_papel.catalogue.domain.model.enums.BookFormat;
import com.relatos_papel.catalogue.infrastructure.repositories.BookRepository;
import com.relatos_papel.catalogue.infrastructure.repositories.GenreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ReplaceBookCommandHandler implements RequestHandler<ReplaceBookCommand, BookDto> {

    private final BookRepository bookRepository;
    private final GenreRepository genreRepository;

    @Override
    public BookDto handle(ReplaceBookCommand request) {
        Book book = bookRepository.findById(request.getBookId())
                .orElseThrow(() -> new RuntimeException("Libro no encontrado"));

        var data = request.getData();

        // PUT: reemplaza todos los campos
        book.setTitle(data.getTitle());
        book.setAuthor(data.getAuthor());
        book.setIsbn(data.getIsbn());
        book.setPrice(data.getPrice());
        book.setStock(data.getStock());
        book.setCoverUrl(data.getCoverUrl());
        book.setDescription(data.getDescription());
        book.setFormat(data.getFormat() != null ? BookFormat.fromValue(data.getFormat()) : BookFormat.AMBOS);
        book.setLanguage(data.getLanguage() != null ? data.getLanguage() : "Español");
        book.setPublicationDate(data.getPublicationDate());
        book.setIsAvailable(data.getIsAvailable() != null ? data.getIsAvailable() : true);

        if (data.getGenreId() != null) {
            book.setGenre(genreRepository.findById(data.getGenreId())
                    .orElseThrow(() -> new RuntimeException("Género no encontrado")));
        }

        Book savedBook = bookRepository.save(book);
        return BookDto.mapToDto(savedBook);
    }

    @Override
    public Class<ReplaceBookCommand> getRequestType() {
        return ReplaceBookCommand.class;
    }
}

