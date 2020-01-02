package com.afs.invoiceapi.exceptions;

public class EmailNotUniqueException extends BaseException {
    public EmailNotUniqueException(String key, String... args) {
        super(key, args);
    }
}
