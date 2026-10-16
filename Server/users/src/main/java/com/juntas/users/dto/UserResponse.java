package com.juntas.users.dto;

import com.juntas.users.model.Role;

public record UserResponse(
        Long id,
        String rut,
        String dv,
        String name,
        String email,
        Role role,
        Long neighborhoodId
) {}