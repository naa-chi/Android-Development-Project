package com.juntas.expenses.utility.dto;

import com.juntas.expenses.utility.model.UtilityType;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;
import java.time.LocalDate;

public record UpdateUtilityExpenseRequest(
        @PastOrPresent(message = "La fecha de emisión no puede ser futura")
        LocalDate emissionDate,

        @PositiveOrZero(message = "El monto no puede ser negativo")
        BigDecimal bill,

        UtilityType utilType,

        Long forUserId,

        @Email(message = "El correo no es válido")
        @Size(max = 120)
        String forEmail,

        Long neighborhoodId
) {}