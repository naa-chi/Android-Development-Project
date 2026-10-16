package com.juntas.users.security;

public record AuthenticatedUser(
        Long userId,
        String email,
        String role,
        String name
) {}