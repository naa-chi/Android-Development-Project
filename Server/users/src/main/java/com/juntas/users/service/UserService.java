package com.juntas.users.service;

import com.juntas.users.dto.UserResponse;
import com.juntas.users.exception.UserNotFoundException;
import com.juntas.users.mapper.UserMapper;
import com.juntas.users.repository.UserRepository;
import com.juntas.users.security.CurrentUserProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {
    private final UserRepository repo;
    private final UserMapper mapper;
    private final CurrentUserProvider currentUser;

    public UserResponse me() {
        Long id = currentUser.getUserId();
        return repo.findById(id)
                .map(mapper::toResponse)
                .orElseThrow(() -> new UserNotFoundException(id));
    }

    public UserResponse findById(Long id) {
        return repo.findById(id)
                .map(mapper::toResponse)
                .orElseThrow(() -> new UserNotFoundException(id));
    }

    public List<UserResponse> findAll() {
        return repo.findAll().stream().map(mapper::toResponse).toList();
    }
}