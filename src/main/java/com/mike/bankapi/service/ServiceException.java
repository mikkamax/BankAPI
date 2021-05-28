package com.mike.bankapi.service;

/**
 * Exception-обертка для всех исключений, возникающих в слое Service
 */
public class ServiceException extends Exception {
    public ServiceException() {
    }

    public ServiceException(String message) {
        super(message);
    }

    public ServiceException(String message, Throwable cause) {
        super(message, cause);
    }
}
