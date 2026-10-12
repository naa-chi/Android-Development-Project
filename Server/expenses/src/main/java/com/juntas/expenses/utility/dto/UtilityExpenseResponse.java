package com.juntas.expenses.utility.dto;

import com.juntas.expenses.utility.model.UtilityType;

import java.math.BigDecimal;
import java.time.LocalDate;

public record UtilityExpenseResponse(
        Long id,
        LocalDate emissionDate,
        BigDecimal bill,
        UtilityType utilType,
        Long forUserId,
        String forEmail,
        Long neighborhoodId
) {}