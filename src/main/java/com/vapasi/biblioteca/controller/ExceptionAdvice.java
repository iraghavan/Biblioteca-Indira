package com.vapasi.biblioteca.controller;

import com.vapasi.biblioteca.exception.NotAValidBookException;
import com.vapasi.biblioteca.exception.NotAValidLibraryNumberException;
import com.vapasi.biblioteca.exception.NotAValidMovieException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ExceptionAdvice
{
    @ExceptionHandler(value = NotAValidBookException.class)
    public ResponseEntity<Object> handleBookNotValidException(NotAValidBookException notAValidBookException) {
        return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(notAValidBookException.getMessage());
    }

    @ExceptionHandler(value = NotAValidMovieException.class)
    public ResponseEntity<Object> handleMovieNotValidException(NotAValidMovieException notAValidMovieException) {
        return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(notAValidMovieException.getMessage());
    }

    @ExceptionHandler(value = NotAValidLibraryNumberException.class)
    public ResponseEntity<Object> handleLibraryNumberNotValidException(NotAValidLibraryNumberException notAValidLibraryNumberException) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(notAValidLibraryNumberException.getMessage());
    }
}

