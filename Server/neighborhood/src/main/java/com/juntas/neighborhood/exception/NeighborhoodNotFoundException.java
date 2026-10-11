package com.juntas.neighborhood.exception;

public class NeighborhoodNotFoundException extends RuntimeException {
    public NeighborhoodNotFoundException(Long id) {
        super("Neighborhood not found: " + id);
    }
}