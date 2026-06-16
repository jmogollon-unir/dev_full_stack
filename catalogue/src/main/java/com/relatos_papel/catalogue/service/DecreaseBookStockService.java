package com.relatos_papel.catalogue.service;

import com.relatos_papel.catalogue.controller.model.StockDecreaseDto;
import com.relatos_papel.catalogue.exception.ResourceNotFoundException;
import com.relatos_papel.catalogue.repository.BookJpaRepository;
import com.relatos_papel.catalogue.repository.model.Book;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class DecreaseBookStockService {

    private final BookJpaRepository bookJpaRepository;

    @Transactional
    public void decreaseStock(Integer bookId, StockDecreaseDto request) {
        if (request.getQuantity() == null || request.getQuantity() <= 0) {
            throw new IllegalArgumentException("La cantidad debe ser mayor a cero.");
        }

        Book book = bookJpaRepository.findById(bookId)
                .orElseThrow(() -> new ResourceNotFoundException("No hay libros con el ID " + bookId));

        int quantity = request.getQuantity();
        if (book.getStock() == null || book.getStock() < quantity) {
            throw new IllegalStateException(
                    "Stock insuficiente para '" + book.getTitle() + "'. Disponible: "
                            + (book.getStock() != null ? book.getStock() : 0) + ", solicitado: " + quantity);
        }

        book.setStock(book.getStock() - quantity);
        bookJpaRepository.save(book);
    }
}
