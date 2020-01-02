package com.afs.invoiceapi.exceptions;

import org.apache.commons.lang3.ArrayUtils;

public class BaseException extends RuntimeException {

    private final String key;
    private final String[] args;

    public BaseException(String key, String... args) {
        this.key = key;
        this.args = args != null ? args : ArrayUtils.EMPTY_STRING_ARRAY;
    }

    public String getKey() {
        return this.key;
    }

    public String[] getArgs() {
        return args;
    }
}