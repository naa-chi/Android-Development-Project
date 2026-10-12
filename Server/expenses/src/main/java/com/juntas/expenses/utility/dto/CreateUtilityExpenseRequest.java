package com.juntas.expenses.utility.dto;

import com.juntas.expenses.utility.model.UtilityType;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;
import java.time.LocalDate;

public record CreateUtilityExpenseRequest(

        @NotNull(message = "La fecha de emisión es obligatoria")
        @PastOrPresent(message = "La fecha no puede ser futura")
        LocalDate emissionDate,

        @NotNull(message = "El monto es obligatorio")
        @PositiveOrZero(message = "El monto no puede ser negativo")
        BigDecimal bill,

        @NotNull(message = "El tipo de suministro es obligatorio")
        UtilityType utilType,

        @NotNull(message = "El vecino es obligatorio")
        Long forUserId,

        @NotBlank(message = "El correo es obligatorio")
        @Email(message = "El correo no es válido")
        @Size(max = 120)
        String forEmail,

        @NotNull(message = "obligatorio")
        Long neighborhoodId
) {}