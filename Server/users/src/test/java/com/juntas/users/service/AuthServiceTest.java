package com.juntas.users.service;

import com.juntas.users.dto.AuthResponse;
import com.juntas.users.dto.LoginRequest;
import com.juntas.users.dto.RegisterRequest;
import com.juntas.users.dto.UserResponse;
import com.juntas.users.mapper.UserMapper;
import com.juntas.users.model.Role;
import com.juntas.users.model.UserModel;
import com.juntas.users.repository.UserRepository;
import com.juntas.users.security.JwtService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock private UserRepository repository;
    @Mock private UserMapper mapper;
    @Mock private PasswordEncoder passwordEncoder;
    @Mock private JwtService jwtService;

    @InjectMocks
    private AuthService service;

    @Test
    void register_persistsUser() {
        var request = new RegisterRequest(
                "12345678", "Test User", "9",
                "test@example.com", "password123", 1L
        );
        when(repository.existsByEmail(any())).thenReturn(false);
        when(repository.existsByRutNumeric(any())).thenReturn(false);
        when(passwordEncoder.encode(any())).thenReturn("hash");
        when(repository.save(any())).thenAnswer(inv -> inv.getArgument(0));
        when(mapper.toResponse(any())).thenReturn(mock(UserResponse.class));
        when(jwtService.generate(any())).thenReturn("token");

        assertNotNull(service.register(request));
        verify(repository).save(any());
    }

    @Test
    void login_returnsToken() {
        var request = new LoginRequest("test@example.com", "password123");
        var user = new UserModel();
        user.setRole(Role.NEIGHBOR);
        when(repository.findByEmail(any())).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(any(), any())).thenReturn(true);
        when(jwtService.generate(any())).thenReturn("token");
        when(mapper.toResponse(any())).thenReturn(mock(UserResponse.class));

        assertNotNull(service.login(request));
        verify(jwtService).generate(user);
    }
}