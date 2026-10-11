package com.juntas.neighborhood.dto;

import jakarta.validation.constraints.*;

public record UpdateNeighborhoodRequest(

        @Size(max = 100, message = "El nombre debe ser menor a 100c")
        String neighborhoodName,

        @Positive(message = "La población debe ser mayor que cero")
        Long populationNumber
) {}