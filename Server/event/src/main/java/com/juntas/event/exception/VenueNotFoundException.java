package com.juntas.event.exception;

public class VenueNotFoundException extends RuntimeException {
    public VenueNotFoundException(Long id) {
        super(id + " no existe.");
    }
}