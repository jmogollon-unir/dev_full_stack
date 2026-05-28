package com.relatos_papel.catalogue.application.book.update;

import com.relatos_papel.catalogue.application.book.common.BookDto;
import com.relatos_papel.catalogue.application.book.common.SaveBookDto;
import com.relatos_papel.catalogue.common.mediator.Request;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UpdateBookCommand implements Request<BookDto> {
    private Long bookId;
    private SaveBookDto data;
}