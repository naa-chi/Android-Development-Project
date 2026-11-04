package com.juntas.venue.service;

import com.juntas.venue.client.NeighborhoodClient;
import com.juntas.venue.client.dto.NeighborhoodDto;
import com.juntas.venue.dto.CreateVenueRequest;
import com.juntas.venue.dto.UpdateVenueRequest;
import com.juntas.venue.dto.VenueResponse;
import com.juntas.venue.exception.VenueNotFoundException;
import com.juntas.venue.mapper.VenueMapper;
import com.juntas.venue.model.VenueModel;
import com.juntas.venue.repository.VenueRepository;
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
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class VenueServiceTest {

    @Mock private VenueRepository repository;
    @Mock private VenueMapper mapper;
    @Mock private NeighborhoodClient neighborhoodClient;

    @InjectMocks
    private VenueService service;

    @Test
    void findById_returnsEntity() {
        when(repository.findById(1L)).thenReturn(Optional.of(new VenueModel()));
        when(mapper.toResponse(any())).thenReturn(mock(VenueResponse.class));
        assertNotNull(service.findById(1L));
        verify(repository).findById(1L);
    }

    @Test
    void findById_throwsWhenMissing() {
        when(repository.findById(99L)).thenReturn(Optional.empty());
        assertThrows(VenueNotFoundException.class, () -> service.findById(99L));
    }

    @Test
    void findAll_returnsList() {
        when(repository.findAll()).thenReturn(List.of(new VenueModel()));
        when(mapper.toResponse(any())).thenReturn(mock(VenueResponse.class));
        assertEquals(1, service.findAll().size());
    }

    @Test
    void create_persistsEntity() {
        when(neighborhoodClient.getById(any())).thenReturn(new NeighborhoodDto(1L, "test"));
        when(mapper.toEntity(any())).thenReturn(new VenueModel());
        when(repository.save(any())).thenAnswer(inv -> inv.getArgument(0));
        when(mapper.toResponse(any())).thenReturn(mock(VenueResponse.class));

        assertNotNull(service.create(mock(CreateVenueRequest.class)));
        verify(repository).save(any());
    }

    @Test
    void update_modifiesExisting() {
        when(repository.findById(1L)).thenReturn(Optional.of(new VenueModel()));
        when(repository.save(any())).thenAnswer(inv -> inv.getArgument(0));
        when(mapper.toResponse(any())).thenReturn(mock(VenueResponse.class));
        doNothing().when(mapper).applyUpdate(any(), any());

        assertNotNull(service.update(1L, mock(UpdateVenueRequest.class)));
        verify(repository).save(any());
    }

    @Test
    void delete_removesExisting() {
        when(repository.existsById(1L)).thenReturn(true);
        service.delete(1L);
        verify(repository).deleteById(1L);
    }

    @Test
    void delete_throwsWhenMissing() {
        when(repository.existsById(99L)).thenReturn(false);
        assertThrows(VenueNotFoundException.class, () -> service.delete(99L));
    }
}