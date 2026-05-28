package com.relatos_papel.orders.infrastructure.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

// "catalogue" debe coincidir exactamente con spring.application.name del microservicio catalogue
@FeignClient(name = "catalogue")
public interface CatalogueClient {

    // Devuelve todos los libros visibles (isAvailable=true filtrado por catalogue)
    @GetMapping("/api/books")
    List<BookSummaryDto> getAllBooks();

    // Obtener un libro por su ID — endpoint agregado en BookController de catalogue
    @GetMapping("/api/books/{id}")
    BookSummaryDto getBookById(@PathVariable Long id);

    // Buscar libros por ISBN usando el endpoint de búsqueda combinada de catalogue
    @GetMapping("/api/books")
    List<BookSummaryDto> getBookByIsbn(@RequestParam String isbn);

    // Actualizar parcialmente un libro
    @PatchMapping("/api/books/{id}")
    void patchBook(@PathVariable("id") Long id, @RequestBody Map<String, Object> fields);
}
