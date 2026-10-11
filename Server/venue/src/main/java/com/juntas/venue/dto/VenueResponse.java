package com.juntas.venue.dto;

import com.juntas.venue.model.VenueType;

import java.math.BigDecimal;

public record VenueResponse(
        Long id,
        String name,
        VenueType type,
        BigDecimal pricePerHour,
        int capacity,
        Long neighbourhoodId
) {}