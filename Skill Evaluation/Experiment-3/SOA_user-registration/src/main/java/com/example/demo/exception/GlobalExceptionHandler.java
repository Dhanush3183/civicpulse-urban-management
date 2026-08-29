package com.example.demo.exception;

import java.util.HashMap;
import java.util.Map; // Inferred
import org.springframework.http.ResponseEntity; // Inferred
import org.springframework.web.bind.annotation.ExceptionHandler; // Inferred
import org.springframework.web.bind.annotation.RestControllerAdvice; // Inferred
import org.springframework.web.bind.MethodArgumentNotValidException; // Inferred

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidation(
            MethodArgumentNotValidException exception) {

        Map<String, String> errors = new HashMap<>();

        exception.getBindingResult()
                .getFieldErrors()
                .forEach(error -> 
                        errors.put(
                                error.getField(),
                                error.getDefaultMessage()
                        )
                );

        return ResponseEntity.badRequest().body(errors);
    }
}