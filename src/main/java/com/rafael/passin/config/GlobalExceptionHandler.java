package com.rafael.passin.config;

import com.rafael.passin.domain.exceptions.AlreadyExistsException;
import com.rafael.passin.domain.exceptions.EventCapacityExceededException;
import com.rafael.passin.domain.exceptions.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<?> handleEventNotFound(ResourceNotFoundException exception) {

        return ResponseEntity.notFound().build();
    }

    @ExceptionHandler(AlreadyExistsException.class)
    public ResponseEntity<?> handleAlreadyExistsException(AlreadyExistsException exception) {

       return ResponseEntity.status(HttpStatus.CONFLICT).build();
    }

    @ExceptionHandler(EventCapacityExceededException.class)
    public ResponseEntity<?> handleEventCapacityExceededException(EventCapacityExceededException exception) {

       return ResponseEntity.status(HttpStatus.CONFLICT).build();
    }
}
