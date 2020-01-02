package com.afs.invoiceapi.exceptions;

public class DomainNotFoundException extends BaseException {

    public DomainNotFoundException(String key, String... args) {
        super(key, args);
    }
}
