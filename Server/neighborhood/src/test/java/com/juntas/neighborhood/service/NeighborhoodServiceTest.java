package com.juntas.neighborhood.service;

import com.juntas.neighborhood.dto.CreateNeighborhoodRequest;
import com.juntas.neighborhood.dto.NeighborhoodResponse;
import com.juntas.neighborhood.dto.UpdateNeighborhoodRequest;
import com.juntas.neighborhood.exception.NeighborhoodAlreadyExistsException;
import com.juntas.neighborhood.exception.NeighborhoodNotFoundException;
import com.juntas.neighborhood.mapper.NeighborhoodMapper;
import com.juntas.neighborhood.model.NeighborhoodModel;
import com.juntas.neighborhood.repository.NeighborhoodRepository;
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
class NeighborhoodServiceTest {

    @Mock private NeighborhoodRepository repository;
    @Mock private NeighborhoodMapper mapper;

    @InjectMocks
    private NeighborhoodService service;

    @Test
    void findById_returnsEntity() {
        when(repository.findById(1L)).thenReturn(Optional.of(new NeighborhoodModel()));
        when(mapper.toResponse(any())).thenReturn(mock(NeighborhoodResponse.class));
        assertNotNull(service.findById(1L));
        verify(repository).findById(1L);
    }

    @Test
    void findById_throwsWhenMissing() {
        when(repository.findById(99L)).thenReturn(Optional.empty());
        assertThrows(NeighborhoodNotFoundException.class, () -> service.findById(99L));
    }

    @Test
    void findAll_returnsList() {
        when(repository.findAll()).thenReturn(List.of(new NeighborhoodModel()));
        when(mapper.toResponse(any())).thenReturn(mock(NeighborhoodResponse.class));
        assertEquals(1, service.findAll().size());
    }

    @Test
    void create_persistsEntity() {
        var request = new CreateNeighborhoodRequest("Test Hood", 1000L);
        when(repository.existsByNeighborhoodName(any())).thenReturn(false);
        when(mapper.toEntity(any())).thenReturn(new NeighborhoodModel());
        when(repository.save(any())).thenAnswer(inv -> inv.getArgument(0));
        when(mapper.toResponse(any())).thenReturn(mock(NeighborhoodResponse.class));
        assertNotNull(service.create(request));
        verify(repository).save(any());
    }

    @Test
    void create_throwsWhenDuplicate() {
        var request = new CreateNeighborhoodRequest("Test Hood", 1000L);
        when(repository.existsByNeighborhoodName(any())).thenReturn(true);
        assertThrows(NeighborhoodAlreadyExistsException.class, () -> service.create(request));
    }

    @Test
    void update_modifiesExisting() {
        when(repository.findById(1L)).thenReturn(Optional.of(new NeighborhoodModel()));
        when(repository.save(any())).thenAnswer(inv -> inv.getArgument(0));
        when(mapper.toResponse(any())).thenReturn(mock(NeighborhoodResponse.class));
        doNothing().when(mapper).applyUpdate(any(), any());
        assertNotNull(service.update(1L, mock(UpdateNeighborhoodRequest.class)));
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
        assertThrows(NeighborhoodNotFoundException.class, () -> service.delete(99L));
    }
}