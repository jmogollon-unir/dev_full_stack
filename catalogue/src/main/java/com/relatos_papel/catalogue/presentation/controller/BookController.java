package com.relatos_papel.catalogue.presentation.controller;

import com.relatos_papel.catalogue.application.book.create.CreateBookCommand;
import com.relatos_papel.catalogue.application.book.delete.DeleteBookCommand;
import com.relatos_papel.catalogue.application.book.update.UpdateBookCommand;
import com.relatos_papel.catalogue.application.book.common.BookDto;
import com.relatos_papel.catalogue.application.book.common.SaveBookDto;
import com.relatos_papel.catalogue.application.book.search.SearchBooksQuery;
import com.relatos_papel.catalogue.common.mediator.Mediator;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/books")
@RequiredArgsConstructor
public class BookController {

    private final Mediator mediator;

    // 1. Crear Libro
    @PostMapping
    public ResponseEntity<BookDto> createBook(@RequestBody SaveBookDto dto) {
        var command = new CreateBookCommand(dto);
        var result = mediator.dispatch(command);
        return new ResponseEntity<>(result, HttpStatus.CREATED);
    }

    // 2. Modificar (PUT o PATCH funcionan igual con nuestra lógica parcial)
    @PatchMapping("/{id}")
    public ResponseEntity<BookDto> updateBook(@PathVariable Long id, @RequestBody SaveBookDto dto) {
        var command = new UpdateBookCommand(id, dto);
        var result = mediator.dispatch(command);
        return ResponseEntity.ok(result);
    }

    // 3. Eliminar Libro
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBook(@PathVariable Long id) {
        var command = new DeleteBookCommand(id);
        mediator.dispatch(command);
        return ResponseEntity.noContent().build();
    }

    // 4. Buscar por atributos de forma individual o combinada
    // Ejemplo de uso: GET /api/books?title=Harry&author=Rowling&popularity=5
    @GetMapping
    public ResponseEntity<List<BookDto>> searchBooks(
            @RequestParam(required = false) String title,
            @RequestParam(required = false) String author,
            @RequestParam(required = false) String isbn,
            @RequestParam(required = false) String category,
            @RequestParam(required = false) Integer popularity) {

        var query = SearchBooksQuery.builder()
                .title(title)
                .author(author)
                .isbn(isbn)
                .category(category)
                .popularity(popularity)
                .build();

        var result = mediator.dispatch(query);
        return ResponseEntity.ok(result);
    }
}