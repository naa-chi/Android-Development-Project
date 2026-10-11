package com.juntas.event.exception;

public class VenueServiceUnavailableException extends RuntimeException {
    public VenueServiceUnavailableException(String message) {
        super(message);
    }
}