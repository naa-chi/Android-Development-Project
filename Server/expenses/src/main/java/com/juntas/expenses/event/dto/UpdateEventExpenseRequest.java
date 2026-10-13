package com.juntas.expenses.event.dto;

import jakarta.validation.constraints.*;

import java.math.BigDecimal;
import java.time.LocalDate;

public record UpdateEventExpenseRequest(

        @PastOrPresent(message = "La fecha de emisión no puede ser futura")
        LocalDate emissionDate,

        @PositiveOrZero(message = "No puede ser negativo")
        BigDecimal bill,

        Long forUserId,

        @Email(message = "El correo no es válido")
        @Size(max = 120)
        String forEmail,

        Long neighborhoodId
) {}