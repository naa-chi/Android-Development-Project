package com.juntas.users.controller;

import com.juntas.users.dto.*;
import com.juntas.users.service.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@WebMvcTest(controllers = UserController.class)
@AutoConfigureMockMvc(addFilters = false)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private UserService service;

    @Test
    void me_returns200() throws Exception {
        when(service.me()).thenReturn(mock(UserResponse.class));

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/users/me"))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    void getById_returns200() throws Exception {
        when(service.findById(1L)).thenReturn(mock(UserResponse.class));

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/users/1"))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    void getAll_returns200() throws Exception {
        when(service.findAll()).thenReturn(List.of(mock(UserResponse.class)));

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/users"))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }
}
