package com.relatos_papel.catalogue.application.book.create;

import com.relatos_papel.catalogue.application.book.common.BookDto;
import com.relatos_papel.catalogue.application.book.common.SaveBookDto;
import com.relatos_papel.catalogue.common.mediator.Request;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CreateBookCommand implements Request<BookDto> {
    private SaveBookDto data;
}