package com.relatos_papel.catalogue.application.book.delete;

import com.relatos_papel.catalogue.common.mediator.RequestHandler;
import com.relatos_papel.catalogue.infrastructure.repositories.BookRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DeleteBookCommandHandler implements RequestHandler<DeleteBookCommand, Void> {

    private final BookRepository bookRepository;

    @Override
    public Void handle(DeleteBookCommand request) {
        bookRepository.deleteById(request.getBookId());
        return null;
    }

    @Override
    public Class<DeleteBookCommand> getRequestType() {
        return DeleteBookCommand.class;
    }
}