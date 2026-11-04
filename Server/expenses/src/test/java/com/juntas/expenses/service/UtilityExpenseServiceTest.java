package com.juntas.expenses.utility.service;

import com.juntas.expenses.exception.UtilityExpenseNotFoundException;
import com.juntas.expenses.utility.dto.CreateUtilityExpenseRequest;
import com.juntas.expenses.utility.dto.UpdateUtilityExpenseRequest;
import com.juntas.expenses.utility.dto.UtilityExpenseResponse;
import com.juntas.expenses.utility.mapper.UtilityExpenseMapper;
import com.juntas.expenses.utility.model.UtilityExpenseModel;
import com.juntas.expenses.utility.repository.UtilityExpenseRepository;
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
class UtilityExpenseServiceTest {

    @Mock private UtilityExpenseRepository repository;
    @Mock private UtilityExpenseMapper mapper;

    @InjectMocks
    private UtilityExpenseService service;

    @Test
    void findById_returnsEntity() {
        when(repository.findById(1L)).thenReturn(Optional.of(new UtilityExpenseModel()));
        when(mapper.toResponse(any())).thenReturn(mock(UtilityExpenseResponse.class));
        assertNotNull(service.findById(1L));
        verify(repository).findById(1L);
    }

    @Test
    void findById_throwsWhenMissing() {
        when(repository.findById(99L)).thenReturn(Optional.empty());
        assertThrows(UtilityExpenseNotFoundException.class, () -> service.findById(99L));
    }

    @Test
    void findAll_returnsList() {
        when(repository.findAll()).thenReturn(List.of(new UtilityExpenseModel()));
        when(mapper.toResponse(any())).thenReturn(mock(UtilityExpenseResponse.class));
        assertEquals(1, service.findAll().size());
    }

    @Test
    void create_persistsEntity() {
        when(mapper.toEntity(any())).thenReturn(new UtilityExpenseModel());
        when(repository.save(any())).thenAnswer(inv -> inv.getArgument(0));
        when(mapper.toResponse(any())).thenReturn(mock(UtilityExpenseResponse.class));
        assertNotNull(service.create(mock(CreateUtilityExpenseRequest.class)));
        verify(repository).save(any());
    }

    @Test
    void update_modifiesExisting() {
        when(repository.findById(1L)).thenReturn(Optional.of(new UtilityExpenseModel()));
        when(repository.save(any())).thenAnswer(inv -> inv.getArgument(0));
        when(mapper.toResponse(any())).thenReturn(mock(UtilityExpenseResponse.class));
        doNothing().when(mapper).applyUpdate(any(), any());
        assertNotNull(service.update(1L, mock(UpdateUtilityExpenseRequest.class)));
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
        assertThrows(UtilityExpenseNotFoundException.class, () -> service.delete(99L));
    }
}