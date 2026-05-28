package com.relatos_papel.catalogue.application.book.decrease;

import com.relatos_papel.catalogue.application.book.common.StockDecreaseDto;
import com.relatos_papel.catalogue.common.mediator.Request;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class DecreaseBookStockCommand implements Request<Void> {
    private final Integer bookId;
    private final StockDecreaseDto data;
}
