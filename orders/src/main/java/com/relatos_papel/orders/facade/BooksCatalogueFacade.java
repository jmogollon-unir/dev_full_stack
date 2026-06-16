package com.relatos_papel.orders.facade;

import com.relatos_papel.orders.exception.OrderValidationException;
import com.relatos_papel.orders.facade.model.BookSummaryDto;
import com.relatos_papel.orders.facade.model.StockDecreaseDto;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class BooksCatalogueFacade {

    private final CatalogueClient catalogueClient;

    public BookSummaryDto getBook(Integer bookId) {
        try {
            return catalogueClient.getBookById(bookId);
        } catch (FeignException.NotFound e) {
            throw new OrderValidationException("El libro con ID " + bookId + " no existe en el catálogo.");
        } catch (FeignException e) {
            throw new OrderValidationException("No se pudo validar el libro con ID " + bookId + " en el microservicio catalogue.");
        }
    }

    public List<BookSummaryDto> getAllBooks() {
        try {
            return catalogueClient.getAllBooks();
        } catch (FeignException e) {
            return List.of();
        }
    }

    public void decreaseBookStock(Integer bookId, Integer quantity) {
        try {
            StockDecreaseDto dto = new StockDecreaseDto();
            dto.setQuantity(quantity);
            catalogueClient.decreaseBookStock(bookId, dto);
        } catch (FeignException.BadRequest e) {
            throw new OrderValidationException("No se pudo actualizar el stock del libro con ID " + bookId + ".");
        } catch (FeignException.NotFound e) {
            throw new OrderValidationException("El libro con ID " + bookId + " no existe en el catálogo.");
        } catch (FeignException e) {
            throw new OrderValidationException("No se pudo actualizar el stock del libro con ID " + bookId + " en el microservicio catalogue.");
        }
    }
}
