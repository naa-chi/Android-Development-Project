package com.juntas.expenses.exception;

public class UtilityExpenseNotFoundException extends RuntimeException {
    public UtilityExpenseNotFoundException(Long id) {
        super("Gasto de suministro no encontrado: " + id);
    }
}