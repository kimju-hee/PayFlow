package com.payflow.pg.support;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(ApiException.class)
    public ResponseEntity<Map<String,Object>> handle(ApiException e) {
        return ResponseEntity.status(e.getStatus()).body(Map.of("success", false, "error", e.getMessage()));
    }
}
