package com.juntas.users.controller;

import com.juntas.users.dto.UserResponse;
import com.juntas.users.security.JwtAuthFilter;
import com.juntas.users.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = UserController.class)
@AutoConfigureMockMvc(addFilters = false)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private UserService service;

    @MockitoBean
    private JwtAuthFilter jwtAuthFilter;      // <── MUST be here, same as AuthControllerTest

    @Test
    void me_returns200() throws Exception {
        when(service.me()).thenReturn(mock(UserResponse.class));
        mockMvc.perform(get("/api/v1/users/me")).andExpect(status().isOk());
    }

    @Test
    void getById_returns200() throws Exception {
        when(service.findById(1L)).thenReturn(mock(UserResponse.class));
        mockMvc.perform(get("/api/v1/users/1")).andExpect(status().isOk());
    }

    @Test
    void getAll_returns200() throws Exception {
        when(service.findAll()).thenReturn(List.of(mock(UserResponse.class)));
        mockMvc.perform(get("/api/v1/users")).andExpect(status().isOk());
    }
}