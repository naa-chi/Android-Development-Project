package com.juntas.expenses.exception;

public class EventExpenseNotFoundException extends RuntimeException {
    public EventExpenseNotFoundException(Long id) {
        super("Gasto de evento no encontrado: " + id);
    }
}