package com.juntas.event.service;

import com.juntas.event.client.VenueClient;
import com.juntas.event.dto.CreateEventRequest;
import com.juntas.event.dto.EventResponse;
import com.juntas.event.dto.UpdateEventRequest;
import com.juntas.event.exception.*;
import com.juntas.event.mapper.EventMapper;
import com.juntas.event.model.EventModel;
import com.juntas.event.repository.EventRepository;
import com.juntas.event.security.CurrentUserProvider;
import feign.FeignException;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class EventService {
    private final EventRepository eventRepo;
    private final EventMapper mapper;
    private final VenueClient venueClient;
    private final CurrentUserProvider currentUser;


    @CircuitBreaker(name = "venues", fallbackMethod = "createFallback")
    public EventResponse create(CreateEventRequest req) {
        validateSchedule(req.startTime(), req.endTime());
        assertNoOverlap(req.venueId(), req.date(), req.startTime(), req.endTime(), null);
        validateVenueExists(req.venueId());

        EventModel event = mapper.toEntity(req);
        event.setNeighborId(currentUser.getUserId());
        return mapper.toResponse(eventRepo.save(event));
    }

    @Transactional(readOnly = true)
    public EventResponse findById(Long id) {
        return eventRepo.findById(id)
                .map(mapper::toResponse)
                .orElseThrow(() -> new EventNotFoundException(id));
    }

    @Transactional(readOnly = true)
    public List<EventResponse> findAll() {
        return eventRepo.findAll().stream()
                .map(mapper::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<EventResponse> findByNeighbor(Long neighborId) {
        return eventRepo.findAll().stream()
                .filter(e -> neighborId.equals(e.getNeighborId()))
                .map(mapper::toResponse)
                .toList();
    }

    @CircuitBreaker(name = "venues", fallbackMethod = "updateFallback")
    public EventResponse update(Long id, UpdateEventRequest req) {
        EventModel existing = eventRepo.findById(id)
                .orElseThrow(() -> new EventNotFoundException(id));

        LocalTime newStart = req.startTime() != null ? req.startTime() : existing.getStartTime();
        LocalTime newEnd   = req.endTime()   != null ? req.endTime()   : existing.getEndTime();
        Long newVenueId    = req.venueId()   != null ? req.venueId()   : existing.getVenueId();
        LocalDate newDate  = req.date()      != null ? req.date()      : existing.getDate();

        validateSchedule(newStart, newEnd);
        assertNoOverlap(newVenueId, newDate, newStart, newEnd, id);

        if (req.venueId() != null && !req.venueId().equals(existing.getVenueId())) {
            validateVenueExists(req.venueId());
        }

        mapper.applyUpdate(req, existing);
        return mapper.toResponse(eventRepo.save(existing));
    }

    public void delete(Long id) {
        if (!eventRepo.existsById(id)) {
            throw new EventNotFoundException(id);
        }
        eventRepo.deleteById(id);
    }

    private void validateSchedule(LocalTime start, LocalTime end) {
        if (!start.isBefore(end)) {
            throw new InvalidScheduleException(
                    "La hora de fin debe ser seguir a a la de inicio.");
        }
    }

    private void assertNoOverlap(Long venueId, LocalDate date,
                                 LocalTime start, LocalTime end, Long excludeId) {
        boolean conflict = eventRepo.findAll().stream()
                .filter(e -> excludeId == null || !e.getId().equals(excludeId))
                .filter(e -> venueId.equals(e.getVenueId()))
                .filter(e -> date.equals(e.getDate()))
                .anyMatch(e -> e.getStartTime().isBefore(end)
                            && e.getEndTime().isAfter(start));

        if (conflict) {
            throw new VenueAlreadyBookedException(venueId, date);
        }
    }

    private void validateVenueExists(Long venueId) {
        try {
            venueClient.getById(venueId);
        } catch (FeignException.NotFound e) {
            throw new VenueNotFoundException(venueId);
        }
    }

    private EventResponse createFallback(CreateEventRequest req, Throwable t) {
        throw new VenueServiceUnavailableException(
                "No se pudo verificar!!!! Intente nuevamente.!!!!!");
    }

    private EventResponse updateFallback(Long id, UpdateEventRequest req, Throwable t) {
        throw new VenueServiceUnavailableException(
                "No se pudo verificar en este momento!!! Intente nuevamente!!!");
    }
}