package com.juntas.venue.dto;

import com.juntas.venue.model.VenueType;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;

public record CreateVenueRequest(

        @NotBlank(message = "El nombre es obligatorio")
        @Size(max = 100, message = "El nombre no puede superar los 100 caracteres")
        String name,

        @NotNull(message = "El tipo de recinto es obligatorio")
        VenueType type,

        @NotNull(message = "El precio por hora es obligatorio")
        @PositiveOrZero(message = "El precio por hora no puede ser negativo")
        BigDecimal pricePerHour,

        @Positive(message = "La capacidad debe ser mayor que cero")
        int capacity,

        @NotNull(message = "El barrio es obligatorio")
        Long neighbourhoodId
) {} // Admittedly AI-Gen'd tests