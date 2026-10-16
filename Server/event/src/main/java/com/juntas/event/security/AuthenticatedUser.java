package com.juntas.event.security;

public record AuthenticatedUser(
        Long userId,
        String email,
        String role,
        String name
) {}