package com.juntas.event.exception;

import java.time.LocalDate;

public class VenueAlreadyBookedException extends RuntimeException {
    public VenueAlreadyBookedException(Long venueId, LocalDate date) {
        super(+ venueId + " ya está reservado");
    }
}