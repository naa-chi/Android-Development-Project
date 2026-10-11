package com.juntas.venue.dto;

import com.juntas.venue.model.VenueType;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;

public record UpdateVenueRequest(

        @Size(max = 100, message = "El nombre no puede superar los 100 caracteres")
        String name,

        VenueType type,

        @PositiveOrZero(message = "El precio por hora no puede ser negativo")
        BigDecimal pricePerHour,

        @Positive(message = "La capacidad debe ser mayor que cero")
        Integer capacity,

        Long neighbourhoodId
) {} // Admittedly AI-Gen'd tests, unfamiliar structure