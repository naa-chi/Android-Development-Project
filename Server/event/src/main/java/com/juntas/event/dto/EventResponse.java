package com.juntas.event.dto;

import com.juntas.event.model.EventType;

import java.time.LocalDate;
import java.time.LocalTime;

public record EventResponse(
        Long id,
        String title,
        String description,
        EventType eventType,
        Long venueId,
        Long neighborId,
        LocalDate date,
        LocalTime startTime,
        LocalTime endTime
) {}