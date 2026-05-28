package com.relatos_papel.catalogue.application.book.delete;

import com.relatos_papel.catalogue.common.mediator.Request;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class DeleteBookCommand implements Request<Void> {
    private Integer bookId;
}