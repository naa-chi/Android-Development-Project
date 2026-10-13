package com.juntas.expenses.event.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public record EventExpenseResponse(
        Long id,
        LocalDate emissionDate,
        BigDecimal bill,
        Long forUserId,
        String forEmail,
        Long neighborhoodId
) {}