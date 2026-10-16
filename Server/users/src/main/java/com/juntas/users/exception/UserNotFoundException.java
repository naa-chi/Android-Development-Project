package com.juntas.users.exception;

public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(Long id) {
        super("Usuario no encontrado. Intenta registrarte!");
    }
}