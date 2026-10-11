package com.juntas.event.controller;

import com.juntas.event.dto.CreateEventRequest;
import com.juntas.event.dto.EventResponse;
import com.juntas.event.dto.UpdateEventRequest;
import com.juntas.event.service.EventService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/events")
@RequiredArgsConstructor
public class EventController {

    private final EventService eventService;

    @PostMapping
    public ResponseEntity<EventResponse> create(@Valid @RequestBody CreateEventRequest req) {
        return ResponseEntity.status(HttpStatus.CREATED).body(eventService.create(req));
    }

    @GetMapping("/{id}")
    public EventResponse getById(@PathVariable Long id) {
        return eventService.findById(id);
    }

    @GetMapping
    public List<EventResponse> getAll() {
        return eventService.findAll();
    }

    @GetMapping("/bookedBy/{neighborId}")
    public List<EventResponse> getByNeighbor(@PathVariable Long neighborId) {
        return eventService.findByNeighbor(neighborId);
    }

    @PutMapping("/{id}")
    public EventResponse update(@PathVariable Long id,
                                @Valid @RequestBody UpdateEventRequest req) {
        return eventService.update(id, req);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        eventService.delete(id);
        return ResponseEntity.noContent().build();
    }
}