package com.afs.invoiceapi.controller.exception;

import com.afs.invoiceapi.exceptions.DomainNotFoundException;
import com.afs.invoiceapi.exceptions.EmailNotUniqueException;
import com.afs.invoiceapi.exceptions.ValidationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Arrays;
import java.util.Locale;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private final MessageSource messageSource;
    private static final Locale EN = new Locale("en");
    private static final Logger LOGGER = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    public GlobalExceptionHandler(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    @ExceptionHandler(DomainNotFoundException.class)
    public final ResponseEntity<ErrorResponse> handleNoAvailableProductException(DomainNotFoundException ex) {
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setTimestamp(System.currentTimeMillis());
        Error error = new Error(messageSource.getMessage(ex.getKey(), ex.getArgs(), EN));
        errorResponse.setErrors(Arrays.asList(error));
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(ValidationException.class)
    public final ResponseEntity<ErrorResponse> handleValidationException(ValidationException ex) {
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setTimestamp(System.currentTimeMillis());
        Error error = new Error(messageSource.getMessage(ex.getKey(), ex.getArgs(), EN));
        errorResponse.setErrors(Arrays.asList(error));
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(EmailNotUniqueException.class)
    public final ResponseEntity<ErrorResponse> handleEmailNotUniqueException(EmailNotUniqueException ex) {
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setTimestamp(System.currentTimeMillis());
        Error error = new Error(messageSource.getMessage(ex.getKey(), ex.getArgs(), EN));
        errorResponse.setErrors(Arrays.asList(error));
        return new ResponseEntity<>(errorResponse, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(DataAccessException.class)
    public final ResponseEntity<ErrorResponse> handleDataAccessException(DataAccessException ex) {
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setTimestamp(System.currentTimeMillis());
        Error error = new Error(ex.getMessage());
        errorResponse.setErrors(Arrays.asList(error));
        LOGGER.error("Exception occurred during database operation exception Caused By:{}", ex);
        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setTimestamp(System.currentTimeMillis());
        errorResponse.setErrors(ex.getBindingResult().getFieldErrors()
                .stream()
                .map(i -> new Error(messageSource.getMessage(i.getDefaultMessage(), i.getArguments(), EN)))
                .collect(Collectors.toList()));
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }
}
