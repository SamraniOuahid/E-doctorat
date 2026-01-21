package com.example.demo.exception;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

/**
 * Global exception handler for all REST controllers.
 * Provides consistent error responses across the entire API.
 */
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

        @ExceptionHandler(ResourceNotFoundException.class)
        public ResponseEntity<ApiError> handleResourceNotFound(
                        ResourceNotFoundException ex, HttpServletRequest request) {
                log.warn("Resource not found: {}", ex.getMessage());

                ApiError error = ApiError.of(
                                HttpStatus.NOT_FOUND.value(),
                                "Not Found",
                                ex.getMessage(),
                                request.getRequestURI());

                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
        }

        @ExceptionHandler(ForbiddenOperationException.class)
        public ResponseEntity<ApiError> handleForbiddenOperation(
                        ForbiddenOperationException ex, HttpServletRequest request) {
                log.warn("Forbidden operation: {}", ex.getMessage());

                ApiError error = ApiError.of(
                                HttpStatus.FORBIDDEN.value(),
                                "Forbidden",
                                ex.getMessage(),
                                request.getRequestURI());

                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(error);
        }

        @ExceptionHandler(AccessDeniedException.class)
        public ResponseEntity<ApiError> handleAccessDenied(
                        AccessDeniedException ex, HttpServletRequest request) {
                log.warn("Access denied: {}", ex.getMessage());

                ApiError error = ApiError.of(
                                HttpStatus.FORBIDDEN.value(),
                                "Access Denied",
                                "Vous n'avez pas les permissions nécessaires pour effectuer cette action",
                                request.getRequestURI());

                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(error);
        }

        @ExceptionHandler(AuthenticationException.class)
        public ResponseEntity<ApiError> handleAuthenticationException(
                        AuthenticationException ex, HttpServletRequest request) {
                log.warn("Authentication failed: {}", ex.getMessage());

                String message = "Email ou mot de passe incorrect";
                if (ex instanceof BadCredentialsException) {
                        message = "Email ou mot de passe incorrect";
                } else if (ex.getMessage() != null && !ex.getMessage().isEmpty()) {
                        message = ex.getMessage();
                }

                ApiError error = ApiError.of(
                                HttpStatus.UNAUTHORIZED.value(),
                                "Unauthorized",
                                message,
                                request.getRequestURI());

                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error);
        }

        @ExceptionHandler(ValidationException.class)
        public ResponseEntity<ApiError> handleValidation(
                        ValidationException ex, HttpServletRequest request) {
                log.warn("Validation error: {}", ex.getMessage());

                ApiError error = ApiError.withValidation(
                                HttpStatus.BAD_REQUEST.value(),
                                "Validation Error",
                                ex.getMessage(),
                                request.getRequestURI(),
                                ex.getErrors());

                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
        }

        @ExceptionHandler(MethodArgumentNotValidException.class)
        public ResponseEntity<ApiError> handleMethodArgumentNotValid(
                        MethodArgumentNotValidException ex, HttpServletRequest request) {
                Map<String, String> errors = new HashMap<>();

                ex.getBindingResult().getAllErrors().forEach(error -> {
                        String fieldName = ((FieldError) error).getField();
                        String errorMessage = error.getDefaultMessage();
                        errors.put(fieldName, errorMessage);
                });

                log.warn("Validation failed for request: {}", errors);

                ApiError error = ApiError.withValidation(
                                HttpStatus.BAD_REQUEST.value(),
                                "Validation Error",
                                "Les données fournies sont invalides",
                                request.getRequestURI(),
                                errors);

                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
        }

        @ExceptionHandler(IllegalArgumentException.class)
        public ResponseEntity<ApiError> handleIllegalArgument(
                        IllegalArgumentException ex, HttpServletRequest request) {
                log.warn("Illegal argument: {}", ex.getMessage());

                ApiError error = ApiError.of(
                                HttpStatus.BAD_REQUEST.value(),
                                "Bad Request",
                                ex.getMessage(),
                                request.getRequestURI());

                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
        }

        @ExceptionHandler(Exception.class)
        public ResponseEntity<ApiError> handleGenericException(
                        Exception ex, HttpServletRequest request) {
                log.error("Unexpected error occurred", ex);

                ApiError error = ApiError.of(
                                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                                "Internal Server Error",
                                "Une erreur inattendue s'est produite. Veuillez réessayer plus tard.",
                                request.getRequestURI());

                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
}
