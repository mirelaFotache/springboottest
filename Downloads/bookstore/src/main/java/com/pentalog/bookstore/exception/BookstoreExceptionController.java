package com.pentalog.bookstore.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class BookstoreExceptionController {

    /**
     * Exception handler
     * @param e exception
     * @return status message NOT FOUND
     */
    @ExceptionHandler(value = BookstoreException.class)
    public ResponseEntity<Object> exception(BookstoreException e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
    }
}