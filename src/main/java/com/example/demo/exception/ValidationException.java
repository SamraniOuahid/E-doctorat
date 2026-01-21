package com.example.demo.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.Map;
import java.util.HashMap;

/**
 * Exception thrown when input validation fails.
 */
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class ValidationException extends RuntimeException {

    private final Map<String, String> errors;

    public ValidationException(String message) {
        super(message);
        this.errors = new HashMap<>();
    }

    public ValidationException(String field, String message) {
        super(message);
        this.errors = new HashMap<>();
        this.errors.put(field, message);
    }

    public ValidationException(Map<String, String> errors) {
        super("Validation failed");
        this.errors = errors;
    }

    public Map<String, String> getErrors() {
        return errors;
    }
}
