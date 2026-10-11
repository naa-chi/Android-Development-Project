package com.juntas.venue.exception;

public class VenueNotFoundException extends RuntimeException {
    public VenueNotFoundException(Long id) {
        super("Venue not found: " + id);
    }
}