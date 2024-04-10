package com.rafael.passin.config;

import com.rafael.passin.domain.exceptions.AlreadyExistsException;
import com.rafael.passin.domain.exceptions.EventCapacityExceededException;
import com.rafael.passin.domain.exceptions.ResourceNotFoundException;
import com.rafael.passin.dto.errors.ErrorResponse;
import com.rafael.passin.dto.errors.FieldErrorDTO;
import com.rafael.passin.dto.errors.GlobalErrorDTO;
import lombok.extern.java.Log;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Log
@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleResourceNotFoundException(ResourceNotFoundException exception) {
        log.severe("Resource not found: " + exception.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    @ExceptionHandler(AlreadyExistsException.class)
    public ResponseEntity<ErrorResponse> handleAlreadyExistsException(AlreadyExistsException exception) {
        log.severe("Conflict: "+ exception.getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT).build();
    }

    @ExceptionHandler(EventCapacityExceededException.class)
    public ResponseEntity<ErrorResponse> handleEventCapacityExceededException(EventCapacityExceededException exception) {
        log.severe("Capacity exceeded: "+ exception.getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT).build();
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ErrorResponse> handleDataIntegrityViolationException(DataIntegrityViolationException exception) {
        log.severe("Data integrity violation: "+ exception.getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT).build();
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {

        List<FieldErrorDTO> fieldErrors = ex.getBindingResult().getFieldErrors().stream()
                .map(error -> new FieldErrorDTO(error.getField(), error.getDefaultMessage()))
                .collect(Collectors.toList());

        List<GlobalErrorDTO> globalErrors = ex.getBindingResult().getGlobalErrors().stream()
                .map(error -> new GlobalErrorDTO(error.getObjectName(), error.getDefaultMessage()))
                .collect(Collectors.toList());

        ErrorResponse errorResponse = new ErrorResponse(fieldErrors, globalErrors);
        return ResponseEntity.badRequest().body(errorResponse);
    }

}
