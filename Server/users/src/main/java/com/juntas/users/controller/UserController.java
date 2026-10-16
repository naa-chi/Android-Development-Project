package com.juntas.users.controller;

import com.juntas.users.dto.UserResponse;
import com.juntas.users.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping("/me")
    public UserResponse me() {
        return userService.me();
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public UserResponse getById(@PathVariable Long id) {
        return userService.findById(id);
    }

    @GetMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    public List<UserResponse> getAll() {
        return userService.findAll();
    }
}