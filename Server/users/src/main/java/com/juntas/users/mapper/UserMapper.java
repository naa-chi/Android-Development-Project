package com.juntas.users.mapper;

import com.juntas.users.dto.UserResponse;
import com.juntas.users.model.UserModel;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {
    public UserResponse toResponse(UserModel entity) {
        return new UserResponse(
                entity.getId(),
                entity.getRutNumeric(),
                entity.getDv(),
                entity.getName(),
                entity.getEmail(),
                entity.getRole(),
                entity.getNeighborhoodId()
        );
    }
}