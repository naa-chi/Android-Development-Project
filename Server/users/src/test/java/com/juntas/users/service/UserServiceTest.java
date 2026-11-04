package com.juntas.users.service;

import com.juntas.users.dto.UserResponse;
import com.juntas.users.exception.UserNotFoundException;
import com.juntas.users.mapper.UserMapper;
import com.juntas.users.model.UserModel;
import com.juntas.users.repository.UserRepository;
import com.juntas.users.security.CurrentUserProvider;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock private UserRepository repository;
    @Mock private UserMapper mapper;
    @Mock private CurrentUserProvider currentUser;

    @InjectMocks
    private UserService service;

    @Test
    void me_returnsUser() {
        when(currentUser.getUserId()).thenReturn(1L);
        when(repository.findById(1L)).thenReturn(Optional.of(new UserModel()));
        when(mapper.toResponse(any())).thenReturn(mock(UserResponse.class));
        assertNotNull(service.me());
    }

    @Test
    void me_throwsWhenMissing() {
        when(currentUser.getUserId()).thenReturn(99L);
        when(repository.findById(99L)).thenReturn(Optional.empty());
        assertThrows(UserNotFoundException.class, () -> service.me());
    }

    @Test
    void findById_returnsUser() {
        when(repository.findById(1L)).thenReturn(Optional.of(new UserModel()));
        when(mapper.toResponse(any())).thenReturn(mock(UserResponse.class));
        assertNotNull(service.findById(1L));
    }

    @Test
    void findById_throwsWhenMissing() {
        when(repository.findById(99L)).thenReturn(Optional.empty());
        assertThrows(UserNotFoundException.class, () -> service.findById(99L));
    }

    @Test
    void findAll_returnsList() {
        when(repository.findAll()).thenReturn(List.of(new UserModel()));
        when(mapper.toResponse(any())).thenReturn(mock(UserResponse.class));
        assertEquals(1, service.findAll().size());
    }
}