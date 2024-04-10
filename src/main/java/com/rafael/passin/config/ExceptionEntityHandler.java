package com.rafael.passin.config;

import com.rafael.passin.domain.exceptions.ResourceNotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ExceptionEntityHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity handleEventNotFound(ResourceNotFoundException exception) {


        return ResponseEntity.notFound().build();

    }
}
