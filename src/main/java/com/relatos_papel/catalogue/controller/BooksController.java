package com.relatos_papel.catalogue.controller;

import com.relatos_papel.catalogue.controller.model.BookDto;
import com.relatos_papel.catalogue.controller.model.SaveBookDto;
import com.relatos_papel.catalogue.controller.model.SearchBooksCriteria;
import com.relatos_papel.catalogue.controller.model.StockDecreaseDto;
import com.relatos_papel.catalogue.service.CreateBooksService;
import com.relatos_papel.catalogue.service.DecreaseBookStockService;
import com.relatos_papel.catalogue.service.DeleteBooksService;
import com.relatos_papel.catalogue.service.GetBooksService;
import com.relatos_papel.catalogue.service.UpdateBooksService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/books")
@RequiredArgsConstructor
public class BooksController {

    private final CreateBooksService createBooksService;
    private final GetBooksService getBooksService;
    private final UpdateBooksService updateBooksService;
    private final DeleteBooksService deleteBooksService;
    private final DecreaseBookStockService decreaseBookStockService;

    @PostMapping
    public ResponseEntity<BookDto> createBook(@RequestBody SaveBookDto dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(createBooksService.createBook(dto));
    }

    @GetMapping("/{id}")
    public ResponseEntity<BookDto> getBookById(@PathVariable Integer id) {
        return ResponseEntity.ok(getBooksService.getBookById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<BookDto> replaceBook(@PathVariable Integer id, @RequestBody SaveBookDto dto) {
        return ResponseEntity.ok(updateBooksService.replaceBook(id, dto));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<BookDto> updateBook(@PathVariable Integer id, @RequestBody SaveBookDto dto) {
        return ResponseEntity.ok(updateBooksService.updateBook(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBook(@PathVariable Integer id) {
        deleteBooksService.deleteBook(id);
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

        SearchBooksCriteria criteria = SearchBooksCriteria.builder()
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

        return ResponseEntity.ok(getBooksService.searchBooks(criteria));
    }

    @PostMapping("/{id}/stock/decrease")
    public ResponseEntity<Void> decreaseStock(@PathVariable Integer id, @RequestBody StockDecreaseDto dto) {
        decreaseBookStockService.decreaseStock(id, dto);
        return ResponseEntity.noContent().build();
    }
}
