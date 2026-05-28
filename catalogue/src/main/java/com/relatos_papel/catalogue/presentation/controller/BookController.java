package com.relatos_papel.catalogue.presentation.controller;

import com.relatos_papel.catalogue.application.book.create.CreateBookCommand;
import com.relatos_papel.catalogue.application.book.delete.DeleteBookCommand;
import com.relatos_papel.catalogue.application.book.update.UpdateBookCommand;
import com.relatos_papel.catalogue.application.book.common.BookDto;
import com.relatos_papel.catalogue.application.book.common.SaveBookDto;
import com.relatos_papel.catalogue.application.book.search.SearchBooksQuery;
import com.relatos_papel.catalogue.common.mediator.Mediator;
import com.relatos_papel.catalogue.infrastructure.repositories.BookRepository;
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
    private final BookRepository bookRepository; // para el GET /{id} directo

    // 1. Crear libro
    @PostMapping
    public ResponseEntity<BookDto> createBook(@RequestBody SaveBookDto dto) {
        var command = new CreateBookCommand(dto);
        var result = mediator.dispatch(command);
        return new ResponseEntity<>(result, HttpStatus.CREATED);
    }

    // 2. Obtener libro por ID — usado por el microservicio orders via Feign
    @GetMapping("/{id}")
    public ResponseEntity<BookDto> getBookById(@PathVariable Long id) {
        return bookRepository.findById(id)
                .map(BookDto::mapToDto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // 3. Modificar (PUT o PATCH funcionan igual con nuestra lógica parcial)
    @PatchMapping("/{id}")
    public ResponseEntity<BookDto> updateBook(@PathVariable Long id, @RequestBody SaveBookDto dto) {
        var command = new UpdateBookCommand(id, dto);
        var result = mediator.dispatch(command);
        return ResponseEntity.ok(result);
    }

    // 4. Eliminar libro
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBook(@PathVariable Long id) {
        var command = new DeleteBookCommand(id);
        mediator.dispatch(command);
        return ResponseEntity.noContent().build();
    }

    // 5. Buscar por atributos de forma individual o combinada
    // Ejemplo: GET /api/books?title=Harry&author=Rowling&isbn=...&category=...&popularity=5
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
