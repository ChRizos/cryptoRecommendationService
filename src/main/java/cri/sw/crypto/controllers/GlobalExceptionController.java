package cri.sw.crypto.controllers;

import cri.sw.crypto.models.ApiError;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;

import java.sql.Timestamp;

@RestControllerAdvice
public class GlobalExceptionController {

    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<ApiError> handleResponseStatusException(ResponseStatusException ex, HttpServletRequest request) {
        ApiError error = new ApiError(
                new Timestamp(System.currentTimeMillis()),
                ex.getStatusCode().value(),
                ex.getStatusCode(),
                ex.getReason(),
                request.getRequestURI()
        );

        return new ResponseEntity<>(error, ex.getStatusCode());
    }
}