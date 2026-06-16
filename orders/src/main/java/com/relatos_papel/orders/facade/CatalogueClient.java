package com.relatos_papel.orders.facade;

import com.relatos_papel.orders.facade.model.BookSummaryDto;
import com.relatos_papel.orders.facade.model.StockDecreaseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@FeignClient(name = "catalogue")
public interface CatalogueClient {

    @GetMapping("/api/books")
    List<BookSummaryDto> getAllBooks();

    @GetMapping("/api/books/{id}")
    BookSummaryDto getBookById(@PathVariable Integer id);

    @PostMapping("/api/books/{id}/stock/decrease")
    void decreaseBookStock(@PathVariable("id") Integer id, @RequestBody StockDecreaseDto dto);
}
