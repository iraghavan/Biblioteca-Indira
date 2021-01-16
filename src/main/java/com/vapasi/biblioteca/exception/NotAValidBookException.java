package com.vapasi.biblioteca.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_ACCEPTABLE)
public class NotAValidBookException extends RuntimeException{
    public NotAValidBookException(String message) {
        super(message);
    }
}
