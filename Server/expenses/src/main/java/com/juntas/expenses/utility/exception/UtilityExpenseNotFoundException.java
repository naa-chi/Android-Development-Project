package com.juntas.expenses.utility.exception;

public class UtilityExpenseNotFoundException extends RuntimeException {
    public UtilityExpenseNotFoundException(Long id) {
        super("Gasto de suministro no encontrado: " + id);
    }
}