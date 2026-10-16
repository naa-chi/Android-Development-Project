package com.juntas.users.dto;

public record AuthResponse(
        String token,
        UserResponse user
) {}