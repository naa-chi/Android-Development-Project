package com.juntas.users.controller;

import com.juntas.users.dto.AuthResponse;
import com.juntas.users.security.JwtAuthFilter;
import com.juntas.users.service.AuthService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = AuthController.class)
@AutoConfigureMockMvc(addFilters = false)
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private AuthService service;

    @MockitoBean
    private JwtAuthFilter jwtAuthFilter;

    @Test
    void register_returns201() throws Exception {
        when(service.register(any())).thenReturn(mock(AuthResponse.class));
        mockMvc.perform(post("/api/v1/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"rut\":\"12345678\",\"name\":\"Test User\",\"dv\":\"9\",\"email\":\"test@example.com\",\"password\":\"password123\",\"neighborhoodId\":1}"))
                .andExpect(status().isCreated());
    }

    @Test
    void login_returns200() throws Exception {
        when(service.login(any())).thenReturn(mock(AuthResponse.class));
        mockMvc.perform(post("/api/v1/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"email\":\"test@example.com\",\"password\":\"password123\"}"))
                .andExpect(status().isOk());
    }
}