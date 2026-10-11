package com.juntas.neighborhood.dto;

public record NeighborhoodResponse(
        Long id,
        String neighborhoodName,
        Long populationNumber
) {}