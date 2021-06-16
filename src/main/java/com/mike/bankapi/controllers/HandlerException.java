package com.mike.bankapi.controllers;

/**
 * Exception-обертка для всех исключений, возникающих в слое Handlers/View
 */
public class HandlerException extends Exception {
    public HandlerException() {
    }

    public HandlerException(String message) {
        super(message);
    }

    public HandlerException(String message, Throwable cause) {
        super(message, cause);
    }
}
