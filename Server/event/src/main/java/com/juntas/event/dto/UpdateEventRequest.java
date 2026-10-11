package com.juntas.event.dto;

import com.juntas.event.model.EventType;
import jakarta.validation.constraints.*;

import java.time.LocalDate;
import java.time.LocalTime;

public record UpdateEventRequest(

        @Size(min = 5, max = 40, message = "El título debe ser entre 5 y 40 caracteres")
        String title,

        @Size(max = 150, message = "La descripción no puede ser mayor a 150 caracteres")
        String description,

        EventType eventType,

        Long venueId,

        @FutureOrPresent(message = "La fecha no puede ser en el pasado")
        LocalDate date,

        LocalTime startTime,

        LocalTime endTime
) {}