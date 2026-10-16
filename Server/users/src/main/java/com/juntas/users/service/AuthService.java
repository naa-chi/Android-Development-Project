package com.juntas.users.service;

import com.juntas.users.dto.AuthResponse;
import com.juntas.users.dto.LoginRequest;
import com.juntas.users.dto.RegisterRequest;
import com.juntas.users.exception.EmailAlreadyExistsException;
import com.juntas.users.exception.InvalidCredentialsException;
import com.juntas.users.mapper.UserMapper;
import com.juntas.users.model.Role;
import com.juntas.users.model.UserModel;
import com.juntas.users.repository.UserRepository;
import com.juntas.users.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class AuthService {
    private final UserRepository repo;
    private final UserMapper mapper;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public AuthResponse register(RegisterRequest req) {
        if (repo.existsByEmail(req.email())) {
            throw new EmailAlreadyExistsException("Email que ya esta registrado: " + req.email());
        }
        if (repo.existsByRut(req.rut())) {
            throw new EmailAlreadyExistsException("Rut que ya estra registrado: " + req.rut());
        }

        UserModel user = new UserModel();
        user.setRutNumeric(req.rut());
        user.setDv(req.dv());
        user.setName(req.name());
        user.setEmail(req.email());
        user.setPasswordHash(passwordEncoder.encode(req.password()));
        user.setRole(Role.NEIGHBOR);
        user.setNeighborhoodId(req.neighborhoodId());

        UserModel saved = repo.save(user);
        String token = jwtService.generate(saved);

        return new AuthResponse(token, mapper.toResponse(saved));
    }

    @Transactional(readOnly = true)
    public AuthResponse login(LoginRequest req) {
        UserModel user = repo.findByEmail(req.email())
                .orElseThrow(InvalidCredentialsException::new);

        if (!passwordEncoder.matches(req.password(), user.getPasswordHash())) {
            throw new InvalidCredentialsException();
        }

        String token = jwtService.generate(user);
        return new AuthResponse(token, mapper.toResponse(user));
    }
}