package com.juntas.expenses.event.service;

import com.juntas.expenses.event.dto.CreateEventExpenseRequest;
import com.juntas.expenses.event.dto.EventExpenseResponse;
import com.juntas.expenses.event.dto.UpdateEventExpenseRequest;
import com.juntas.expenses.event.mapper.EventExpenseMapper;
import com.juntas.expenses.event.model.EventExpenseModel;
import com.juntas.expenses.event.repository.EventExpenseRepository;
import com.juntas.expenses.exception.EventExpenseNotFoundException;
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
class EventExpenseServiceTest {

    @Mock private EventExpenseRepository repository;
    @Mock private EventExpenseMapper mapper;

    @InjectMocks
    private EventExpenseService service;

    @Test
    void findById_returnsEntity() {
        when(repository.findById(1L)).thenReturn(Optional.of(new EventExpenseModel()));
        when(mapper.toResponse(any())).thenReturn(mock(EventExpenseResponse.class));
        assertNotNull(service.findById(1L));
        verify(repository).findById(1L);
    }

    @Test
    void findById_throwsWhenMissing() {
        when(repository.findById(99L)).thenReturn(Optional.empty());
        assertThrows(EventExpenseNotFoundException.class, () -> service.findById(99L));
    }

    @Test
    void findAll_returnsList() {
        when(repository.findAll()).thenReturn(List.of(new EventExpenseModel()));
        when(mapper.toResponse(any())).thenReturn(mock(EventExpenseResponse.class));
        assertEquals(1, service.findAll().size());
    }

    @Test
    void create_persistsEntity() {
        when(mapper.toEntity(any())).thenReturn(new EventExpenseModel());
        when(repository.save(any())).thenAnswer(inv -> inv.getArgument(0));
        when(mapper.toResponse(any())).thenReturn(mock(EventExpenseResponse.class));
        assertNotNull(service.create(mock(CreateEventExpenseRequest.class)));
        verify(repository).save(any());
    }

    @Test
    void update_modifiesExisting() {
        when(repository.findById(1L)).thenReturn(Optional.of(new EventExpenseModel()));
        when(repository.save(any())).thenAnswer(inv -> inv.getArgument(0));
        when(mapper.toResponse(any())).thenReturn(mock(EventExpenseResponse.class));
        doNothing().when(mapper).applyUpdate(any(), any());
        assertNotNull(service.update(1L, mock(UpdateEventExpenseRequest.class)));
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
        assertThrows(EventExpenseNotFoundException.class, () -> service.delete(99L));
    }
}