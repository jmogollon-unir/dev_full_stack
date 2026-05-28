package com.relatos_papel.orders.application.order.create.event;

import com.relatos_papel.orders.application.order.common.SaveOrderDto;
import com.relatos_papel.orders.infrastructure.feign.CatalogueClient;
import com.relatos_papel.orders.infrastructure.feign.BookSummaryDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@RequiredArgsConstructor
@Slf4j
public class OrderCreatedEventListener {

    private final CatalogueClient catalogueClient;

    @Async
    @EventListener
    public void onOrderCreated(OrderCreatedEvent event) {
        log.info("Handling OrderCreatedEvent: updating stock for {} items", event.getItems().size());

        for (SaveOrderDto.OrderItemInput item : event.getItems()) {
            try {
                BookSummaryDto book = catalogueClient.getBookById(item.getBookId());
                if (book != null && book.getStock() != null) {
                    int newStock = book.getStock() - item.getQuantity();
                    catalogueClient.patchBook(item.getBookId(), Map.of("stock", newStock));
                    log.info("Stock actualziado del libro {}: nuevo stock es {}", item.getBookId(), newStock);
                }
            } catch (Exception e) {
                log.error("Falla en la actualziación de stock del libro {}: {}", item.getBookId(), e.getMessage());
            }
        }
    }
}

