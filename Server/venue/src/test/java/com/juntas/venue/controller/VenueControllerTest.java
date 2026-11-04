package com.juntas.venue.controller;

import com.juntas.venue.dto.VenueResponse;
import com.juntas.venue.service.VenueService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = VenueController.class)
@AutoConfigureMockMvc(addFilters = false)
class VenueControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private VenueService service;

    @Test
    void getById_returns200() throws Exception {
        when(service.findById(1L)).thenReturn(mock(VenueResponse.class));
        mockMvc.perform(get("/api/v1/venues/1")).andExpect(status().isOk());
    }

    @Test
    void getAll_returns200() throws Exception {
        when(service.findAll()).thenReturn(List.of(mock(VenueResponse.class)));
        mockMvc.perform(get("/api/v1/venues")).andExpect(status().isOk());
    }

    @Test
    void create_returns201() throws Exception {
        when(service.create(any())).thenReturn(mock(VenueResponse.class));
        mockMvc.perform(post("/api/v1/venues")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"Test Venue\",\"type\":\"HALL\",\"pricePerHour\":100,\"capacity\":50,\"neighbourhoodId\":1,\"venueSizeMSquared\":100}"))
                .andExpect(status().isCreated());
    }

    @Test
    void update_returns200() throws Exception {
        when(service.update(eq(1L), any())).thenReturn(mock(VenueResponse.class));
        mockMvc.perform(put("/api/v1/venues/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isOk());
    }

    @Test
    void delete_returns204() throws Exception {
        doNothing().when(service).delete(1L);
        mockMvc.perform(delete("/api/v1/venues/1")).andExpect(status().isNoContent());
    }
}