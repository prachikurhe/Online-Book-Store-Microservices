package com.codewithsachin.auth.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Object> handleRuntimeException(RuntimeException ex) {
        return new ResponseEntity<>(buildResponse("Runtime Exception", ex), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleException(Exception ex) {
        return new ResponseEntity<>(buildResponse("Generic Exception", ex), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private Map<String, Object> buildResponse(String type, Exception ex) {
        Map<String, Object> body = new HashMap<>();
        body.put("error", type);
        body.put("message", ex.getMessage());
        body.put("timestamp", Instant.now());
        return body;
    }
}
