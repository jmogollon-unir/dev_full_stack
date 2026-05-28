package com.relatos_papel.orders.common.mediator;

public interface RequestHandler<T extends Request<R>, R> {
    R handle(T request);
    Class<T> getRequestType(); // Necesario para el mapa de tu Mediator
}