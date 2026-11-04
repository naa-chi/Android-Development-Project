package com.juntas.event.service;

import com.juntas.event.client.VenueClient;
import com.juntas.event.client.dto.VenueDto;
import com.juntas.event.dto.CreateEventRequest;
import com.juntas.event.dto.EventResponse;
import com.juntas.event.dto.UpdateEventRequest;
import com.juntas.event.exception.EventNotFoundException;
import com.juntas.event.mapper.EventMapper;
import com.juntas.event.model.EventModel;
import com.juntas.event.model.EventType;
import com.juntas.event.repository.EventRepository;
import com.juntas.event.security.CurrentUserProvider;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalTime;
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
class EventServiceTest {

    @Mock private EventRepository repository;
    @Mock private EventMapper mapper;
    @Mock private VenueClient venueClient;
    @Mock private CurrentUserProvider currentUser;

    @InjectMocks
    private EventService service;

    @Test
    void findById_returnsEntity() {
        when(repository.findById(1L)).thenReturn(Optional.of(new EventModel()));
        when(mapper.toResponse(any())).thenReturn(mock(EventResponse.class));
        assertNotNull(service.findById(1L));
        verify(repository).findById(1L);
    }

    @Test
    void findById_throwsWhenMissing() {
        when(repository.findById(99L)).thenReturn(Optional.empty());
        assertThrows(EventNotFoundException.class, () -> service.findById(99L));
    }

    @Test
    void findAll_returnsList() {
        when(repository.findAll()).thenReturn(List.of(new EventModel()));
        when(mapper.toResponse(any())).thenReturn(mock(EventResponse.class));
        assertEquals(1, service.findAll().size());
    }

    @Test
    void create_persistsEntity() {
        var request = new CreateEventRequest(
                "Test Title",
                "Test description",
                EventType.PARTY,
                1L,
                LocalDate.of(2030, 1, 1),
                LocalTime.of(10, 0),
                LocalTime.of(12, 0)
        );
        when(venueClient.getById(any())).thenReturn(new VenueDto(1L, "test"));
        when(mapper.toEntity(any())).thenReturn(new EventModel());
        when(repository.save(any())).thenAnswer(inv -> inv.getArgument(0));
        when(mapper.toResponse(any())).thenReturn(mock(EventResponse.class));
        when(currentUser.getUserId()).thenReturn(1L);

        assertNotNull(service.create(request));
        verify(repository).save(any());
    }

    @Test
    void update_modifiesExisting() {
        var request = new UpdateEventRequest(
                null, null, null, null, null,
                LocalTime.of(10, 0),
                LocalTime.of(12, 0)
        );
        when(repository.findById(1L)).thenReturn(Optional.of(new EventModel()));
        when(repository.save(any())).thenAnswer(inv -> inv.getArgument(0));
        when(mapper.toResponse(any())).thenReturn(mock(EventResponse.class));
        doNothing().when(mapper).applyUpdate(any(), any());

        assertNotNull(service.update(1L, request));
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
        assertThrows(EventNotFoundException.class, () -> service.delete(99L));
    }
}