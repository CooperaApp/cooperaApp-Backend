package com.coopera.cooperaApp.utilities;

import com.coopera.cooperaApp.exceptions.CooperaException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler
    public ResponseEntity<String> handleExceptions(CooperaException e){
        return ResponseEntity.badRequest().body(e.getMessage());
    }
}
