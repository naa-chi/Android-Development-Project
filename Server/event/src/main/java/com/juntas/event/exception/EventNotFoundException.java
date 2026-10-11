package com.juntas.event.exception;

public class EventNotFoundException extends RuntimeException {
    public EventNotFoundException(Long id) {
        super("Evento no encontrado: " + id);
    }
}