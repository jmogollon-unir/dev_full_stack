package com.relatos_papel.catalogue.application.book.decrease;

import com.relatos_papel.catalogue.common.exception.ResourceNotFoundException;
import com.relatos_papel.catalogue.common.mediator.RequestHandler;
import com.relatos_papel.catalogue.domain.model.Book;
import com.relatos_papel.catalogue.infrastructure.repositories.BookRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class DecreaseBookStockCommandHandler implements RequestHandler<DecreaseBookStockCommand, Void> {

    private final BookRepository bookRepository;

    @Override
    @Transactional
    public Void handle(DecreaseBookStockCommand request) {
        if (request.getData().getQuantity() == null || request.getData().getQuantity() <= 0) {
            throw new IllegalArgumentException("La cantidad debe ser mayor a cero.");
        }

        Book book = bookRepository.findById(request.getBookId())
                .orElseThrow(() -> new ResourceNotFoundException("No hay libros con el ID " + request.getBookId()));

        int quantity = request.getData().getQuantity();
        if (book.getStock() == null || book.getStock() < quantity) {
            throw new IllegalStateException(
                    "Stock insuficiente para '" + book.getTitle() + "'. Disponible: "
                            + (book.getStock() != null ? book.getStock() : 0)
                            + ", solicitado: " + quantity);
        }

        book.setStock(book.getStock() - quantity);
        bookRepository.save(book);
        return null;
    }

    @Override
    public Class<DecreaseBookStockCommand> getRequestType() {
        return DecreaseBookStockCommand.class;
    }
}
