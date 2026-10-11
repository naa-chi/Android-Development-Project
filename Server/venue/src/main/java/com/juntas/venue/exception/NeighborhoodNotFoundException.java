package com.juntas.venue.exception;

public class NeighborhoodNotFoundException extends RuntimeException {
    public NeighborhoodNotFoundException(Long id) {
        super(id + " no existe.");
    }
}