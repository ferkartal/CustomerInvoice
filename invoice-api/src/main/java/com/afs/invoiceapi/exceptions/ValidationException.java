package com.afs.invoiceapi.exceptions;

public class ValidationException extends BaseException {
    public ValidationException(String key, String... args) {
        super(key, args);
    }
}
