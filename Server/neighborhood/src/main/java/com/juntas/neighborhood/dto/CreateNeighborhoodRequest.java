package com.juntas.neighborhood.dto;

import jakarta.validation.constraints.*;

public record CreateNeighborhoodRequest(
        @NotBlank(message = "El nombre es obligatorio")
        @Size(max = 100, message = "No puede sueprar 100")
        String neighborhoodName,

        @Positive(message = "La población debe ser mayor que cero")
        Long populationNumber //Not sure if we need this so I'll keep it nullable
) {}