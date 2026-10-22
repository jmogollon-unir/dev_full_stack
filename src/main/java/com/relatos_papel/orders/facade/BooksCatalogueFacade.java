package com.relatos_papel.orders.facade;

import com.relatos_papel.catalogue.controller.model.BookDto;
import com.relatos_papel.catalogue.controller.model.SearchBooksCriteria;
import com.relatos_papel.catalogue.controller.model.StockDecreaseDto;
import com.relatos_papel.catalogue.service.DecreaseBookStockService;
import com.relatos_papel.catalogue.service.GetBooksService;
import com.relatos_papel.orders.exception.OrderValidationException;
import com.relatos_papel.orders.facade.model.BookSummaryDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Acceso de "orders" al catálogo. Al ser una sola aplicación, ya no hay llamadas HTTP (Feign/Eureka):
 * se invocan directamente los servicios del módulo catalogue dentro de la misma transacción.
 */
@Component
@RequiredArgsConstructor
public class BooksCatalogueFacade {

    private final GetBooksService getBooksService;
    private final DecreaseBookStockService decreaseBookStockService;

    public BookSummaryDto getBook(Integer bookId) {
        try {
            return toSummary(getBooksService.getBookById(bookId));
        } catch (com.relatos_papel.catalogue.exception.ResourceNotFoundException e) {
            throw new OrderValidationException("El libro con ID " + bookId + " no existe en el catálogo.");
        }
    }

    public List<BookSummaryDto> getAllBooks() {
        return getBooksService.searchBooks(SearchBooksCriteria.builder().build())
                .stream()
                .map(this::toSummary)
                .toList();
    }

    public void decreaseBookStock(Integer bookId, Integer quantity) {
        StockDecreaseDto dto = new StockDecreaseDto();
        dto.setQuantity(quantity);
        try {
            decreaseBookStockService.decreaseStock(bookId, dto);
        } catch (com.relatos_papel.catalogue.exception.ResourceNotFoundException e) {
            throw new OrderValidationException("El libro con ID " + bookId + " no existe en el catálogo.");
        } catch (IllegalArgumentException | IllegalStateException e) {
            throw new OrderValidationException("No se pudo actualizar el stock del libro con ID " + bookId + ": " + e.getMessage());
        }
    }

    private BookSummaryDto toSummary(BookDto book) {
        BookSummaryDto dto = new BookSummaryDto();
        dto.setBookId(book.getBookId());
        dto.setTitle(book.getTitle());
        dto.setAuthor(book.getAuthor());
        dto.setIsbn(book.getIsbn());
        dto.setPrice(book.getPrice());
        dto.setStock(book.getStock());
        dto.setIsAvailable(book.getIsAvailable());
        return dto;
    }
}
