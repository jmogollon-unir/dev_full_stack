package com.relatos_papel.gateway_secured.service.exception;

public class InvalidSessionException extends Exception {
    public InvalidSessionException(String message) {
        super(message);
    }
}
