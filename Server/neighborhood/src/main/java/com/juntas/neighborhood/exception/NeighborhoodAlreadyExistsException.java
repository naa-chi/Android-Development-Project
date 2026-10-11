package com.juntas.neighborhood.exception;

public class NeighborhoodAlreadyExistsException extends RuntimeException {
    public NeighborhoodAlreadyExistsException(String name) {
        super("Neighborhood already exists: " + name);
    }
}