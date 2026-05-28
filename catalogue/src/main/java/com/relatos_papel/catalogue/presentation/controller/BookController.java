package com.relatos_papel.catalogue.presentation.controller;

import com.relatos_papel.catalogue.application.book.common.BookDto;
import com.relatos_papel.catalogue.application.book.common.SaveBookDto;
import com.relatos_papel.catalogue.application.book.common.StockDecreaseDto;
import com.relatos_papel.catalogue.application.book.create.CreateBookCommand;
import com.relatos_papel.catalogue.application.book.decrease.DecreaseBookStockCommand;
import com.relatos_papel.catalogue.application.book.delete.DeleteBookCommand;
import com.relatos_papel.catalogue.application.book.replace.ReplaceBookCommand;
import com.relatos_papel.catalogue.application.book.search.SearchBooksQuery;
import com.relatos_papel.catalogue.application.book.update.UpdateBookCommand;
import com.relatos_papel.catalogue.common.exception.ResourceNotFoundException;
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
    public ResponseEntity<BookDto> getBookById(@PathVariable Integer id) {
        return bookRepository.findById(id)
                .map(BookDto::mapToDto)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new ResourceNotFoundException("No hay libros con el ID " + id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<BookDto> replaceBook(@PathVariable Integer id, @RequestBody SaveBookDto dto) {
        var command = new ReplaceBookCommand(id, dto);
        var result = mediator.dispatch(command);
        return ResponseEntity.ok(result);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<BookDto> updateBook(@PathVariable Integer id, @RequestBody SaveBookDto dto) {
        var command = new UpdateBookCommand(id, dto);
        var result = mediator.dispatch(command);
        return ResponseEntity.ok(result);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBook(@PathVariable Integer id) {
        var command = new DeleteBookCommand(id);
        mediator.dispatch(command);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<List<BookDto>> searchBooks(
            @RequestParam(required = false) String title,
            @RequestParam(required = false) String author,
            @RequestParam(required = false) String isbn,
            @RequestParam(required = false) Integer category,
            @RequestParam(required = false) String categoryName,
            @RequestParam(required = false) LocalDate publicationDate,
            @RequestParam(required = false) Integer rating,
            @RequestParam(required = false) Boolean visible,
            @RequestParam(required = false) Integer popularity) {

        var query = SearchBooksQuery.builder()
                .title(title)
                .author(author)
                .isbn(isbn)
                .category(category)
                .categoryName(categoryName)
                .publicationDate(publicationDate)
                .rating(rating)
                .visible(visible)
                .popularity(popularity)
                .build();

        var result = mediator.dispatch(query);
        return ResponseEntity.ok(result);
    }

    @PostMapping("/{id}/stock/decrease")
    public ResponseEntity<Void> decreaseStock(@PathVariable Integer id, @RequestBody StockDecreaseDto dto) {
        var command = new DecreaseBookStockCommand(id, dto);
        mediator.dispatch(command);
        return ResponseEntity.noContent().build();
    }
}
