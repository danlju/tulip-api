package com.danlju.tulip.api.controller;

import com.danlju.tulip.core.domain.exceptions.IllegalBuildStateTransitionException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;

@RestControllerAdvice
public class ApiExceptionHandler {

    @ExceptionHandler(IllegalBuildStateTransitionException.class)
    public ResponseEntity<?> handleIllegalTransition(IllegalBuildStateTransitionException ex) {
        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(Map.of(
                        "error", "INVALID_STATE_TRANSITION",
                        "message", ex.getMessage()
                ));
    }
}
