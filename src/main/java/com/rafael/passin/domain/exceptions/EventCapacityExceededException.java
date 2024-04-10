package com.rafael.passin.domain.exceptions;

public class EventCapacityExceededException extends RuntimeException {

    public EventCapacityExceededException(String message) {
        super(message);
    }
}
