package com.juntas.expenses.event.dto;

import jakarta.validation.constraints.*;

import java.math.BigDecimal;
import java.time.LocalDate;

public record CreateEventExpenseRequest(

        @NotNull(message = "La fecha de emisión es obligatoria")
        @PastOrPresent(message = "La fecha de emisión no puede ser futura")
        LocalDate emissionDate,

        @NotNull(message = "El monto es obligatorio")
        @PositiveOrZero(message = "El monto no puede ser negativo")
        BigDecimal bill,

        @NotNull(message = "El vecino es obligatorio")
        Long forUserId,

        @NotBlank(message = "El correo es obligatorio")
        @Email(message = "El correo no es válido")
        @Size(max = 120)
        String forEmail,

        @NotNull(message = "(HELLO I'M YOUR) Neighbor/hood (GOODBYE! SEE YOU LATER!)es obligatorio")
        Long neighborhoodId
) {}