package org.test.editor.presentation.excpetion;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.server.ResponseStatusException;
import org.test.editor.core.exception.DuplicateResourceException;
import org.test.editor.core.exception.InvalidTokenException;
import org.test.editor.core.exception.ResourceNotFoundException;
import org.test.editor.util.ApiResponse;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.List;
import java.util.stream.Collectors;

@ControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<List<String>>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        List<String> errors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .collect(Collectors.toList());
        logger.warn("Validation failed: {}", errors);
        ApiResponse<List<String>> response = new ApiResponse<>(
                "Validation failed",
                "error",
                errors
        );
        return ResponseEntity.badRequest().body(response);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ApiResponse<String>> handleDataIntegrityViolation(DataIntegrityViolationException e) {
        logger.error("Data integrity violation: {}", e.getMessage(), e);
        ApiResponse<String> response = new ApiResponse<>(
                "Data integrity violation: ",
                "error"
        );
        return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<String>> handleGeneralException(Exception e) {
        logger.error("An unexpected error occurred", e.getMessage());
        ApiResponse<String> response = new ApiResponse<>(
                "An unexpected error occurred: " ,
                "error"
        );
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }

    @ExceptionHandler(InvalidTokenException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ResponseEntity<ApiResponse> handleInvalidTokenException(InvalidTokenException ex) {
        logger.warn("Invalid token: {}", ex.getMessage());
        ApiResponse response = new ApiResponse(ex.getMessage(), "error");
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
    }

    @ExceptionHandler(GeneralSecurityException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<ApiResponse> handleGeneralSecurityException(GeneralSecurityException ex) {
        logger.warn("Security issue: {}", ex.getMessage());
        ApiResponse response = new ApiResponse("Security issue: " + ex.getMessage(), "error");
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }

    @ExceptionHandler(IOException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<ApiResponse> handleIOException(IOException ex) {
        logger.error("IO issue: {}", ex.getMessage());
        ApiResponse response = new ApiResponse("IO issue: ", "error");
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }

    @ExceptionHandler(AuthorizationDeniedException.class)
    public ResponseEntity<ApiResponse> handleRuntimeException(AuthorizationDeniedException ex) {
        logger.warn("authorization denied occurred: {}", ex.getMessage());
        ApiResponse response = new ApiResponse("An unexpected error occurred: " + ex.getMessage(), "error");
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);

    }
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<String> handleResourceNotFoundException(ResourceNotFoundException ex) {
        logger.warn("resource not found: {}", ex.getMessage());
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
    }
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ApiResponse> handleRuntimeException(RuntimeException ex) {
        logger.error("An unexpected error occurred: {}", ex.getMessage());
        ApiResponse response = new ApiResponse("An unexpected error occurred: " , "error");
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);

    }
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ApiResponse> handleRuntimeException(AccessDeniedException ex) {
        logger.warn("access denied: {}", ex.getMessage());
        ApiResponse response = new ApiResponse("An unexpected error occurred: ", "error");
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);

    }
    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<ApiResponse<String>> handleResponseStatusException(ResponseStatusException ex) {
        ApiResponse<String> response = new ApiResponse<>(
                ex.getReason(),
                "error"
        );
        return ResponseEntity.status(ex.getStatusCode()).body(response);
    }
    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ApiResponse<String>> handleBadCredentialsException(BadCredentialsException ex) {
        logger.warn("bad credentials: {}", ex.getMessage());
        ApiResponse<String> response = new ApiResponse<>(
                ex.getMessage(),
                "error"
        );
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);
    }

    @ExceptionHandler(DuplicateResourceException.class)
    public ResponseEntity<ApiResponse<String>> handleDuplicateResourceException(DuplicateResourceException ex) {
        logger.warn("duplicate resource: {}", ex.getMessage());
        ApiResponse<String> response = new ApiResponse<>(
                ex.getMessage(),
                "error"
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }


}