package com.example.demo.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Exception thrown when an operation is forbidden for the current user.
 */
@ResponseStatus(HttpStatus.FORBIDDEN)
public class ForbiddenOperationException extends RuntimeException {

    public ForbiddenOperationException(String message) {
        super(message);
    }

    public ForbiddenOperationException(String operation, String reason) {
        super(String.format("Operation '%s' forbidden: %s", operation, reason));
    }
}
