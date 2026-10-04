package com.danlju.tulip.api.controller;

import com.danlju.tulip.core.domain.exceptions.IllegalBuildStateTransitionException;
import com.danlju.tulip.core.domain.exceptions.BuildNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;

@RestControllerAdvice
public class ApiExceptionHandler {

    @ExceptionHandler(BuildNotFoundException.class)
    public ResponseEntity<?> handleBuildNotFound(BuildNotFoundException ex) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(Map.of(
                        "error", "BUILD_NOT_FOUND",
                        "message", ex.getMessage()
                ));
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<?> handleInvalidPathParameter(MethodArgumentTypeMismatchException ex) {
        return ResponseEntity
                .badRequest()
                .body(Map.of(
                        "error", "INVALID_REQUEST",
                        "message", "Invalid value for path parameter: " + ex.getName()
                ));
    }

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
