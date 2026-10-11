package com.juntas.venue.exception;

public class VenueAlreadyExistsException extends RuntimeException {
    public VenueAlreadyExistsException(String name) {
        super("Venue already exists: " + name);
    }
}