package com.juntas.event.dto;

import com.juntas.event.model.EventType;
import jakarta.validation.constraints.*;

import java.time.LocalDate;
import java.time.LocalTime;

public record CreateEventRequest(
    @NotBlank(message = "El título es obligatorio")
    @Size(min = 5, max = 40, message = "El título debe tener entre 5 y 40 caracteres")
    String title,

    @NotBlank(message = "La descripción es obligatoria")
    @Size(max = 150, message = "La descripción no puede superar los 150 caracteres")
    String description,

    @NotNull(message = "El tipo de evento es obligatorio")
    EventType eventType,

    @NotNull(message = "El recinto es obligatorio")
    Long venueId,

    @NotNull(message = "La fecha es obligatoria")
    @FutureOrPresent(message = "La fecha no puede ser en el pasado")
    LocalDate date,

    @NotNull(message = "La hora de inicio es obligatoria")
    LocalTime startTime,

    @NotNull(message = "La hora de fin es obligatoria")
    LocalTime endTime
) {}