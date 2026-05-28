package com.relatos_papel.catalogue.presentation.controller;

import com.relatos_papel.catalogue.application.book.create.CreateBookCommand;
import com.relatos_papel.catalogue.application.book.delete.DeleteBookCommand;
import com.relatos_papel.catalogue.application.book.update.UpdateBookCommand;
import com.relatos_papel.catalogue.application.book.replace.ReplaceBookCommand;
import com.relatos_papel.catalogue.application.book.common.BookDto;
import com.relatos_papel.catalogue.application.book.common.SaveBookDto;
import com.relatos_papel.catalogue.application.book.search.SearchBooksQuery;
import com.relatos_papel.catalogue.common.mediator.Mediator;
import com.relatos_papel.catalogue.infrastructure.repositories.BookRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/books")
@RequiredArgsConstructor
public class BookController {

    private final Mediator mediator;
    private final BookRepository bookRepository;

    @PostMapping
    public ResponseEntity<BookDto> createBook(@RequestBody SaveBookDto dto) {
        var command = new CreateBookCommand(dto);
        var result = mediator.dispatch(command);
        return new ResponseEntity<>(result, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<BookDto> getBookById(@PathVariable Long id) {
        return bookRepository.findById(id)
                .map(BookDto::mapToDto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<BookDto> replaceBook(@PathVariable Long id, @RequestBody SaveBookDto dto) {
        var command = new ReplaceBookCommand(id, dto);
        var result = mediator.dispatch(command);
        return ResponseEntity.ok(result);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<BookDto> updateBook(@PathVariable Long id, @RequestBody SaveBookDto dto) {
        var command = new UpdateBookCommand(id, dto);
        var result = mediator.dispatch(command);
        return ResponseEntity.ok(result);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBook(@PathVariable Long id) {
        var command = new DeleteBookCommand(id);
        mediator.dispatch(command);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<List<BookDto>> searchBooks(
            @RequestParam(required = false) String title,
            @RequestParam(required = false) String author,
            @RequestParam(required = false) String isbn,
            @RequestParam(required = false) Long category,
            @RequestParam(required = false) LocalDate publicationDate,
            @RequestParam(required = false) Integer rating,
            @RequestParam(required = false) Boolean visible,
            @RequestParam(required = false) Integer popularity) {

        var query = SearchBooksQuery.builder()
                .title(title)
                .author(author)
                .isbn(isbn)
                .category(category)
                .publicationDate(publicationDate)
                .rating(rating)
                .visible(visible)
                .popularity(popularity)
                .build();

        var result = mediator.dispatch(query);
        return ResponseEntity.ok(result);
    }
}
