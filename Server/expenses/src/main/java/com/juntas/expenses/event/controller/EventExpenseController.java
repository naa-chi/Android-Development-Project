package com.juntas.expenses.event.controller;

import com.juntas.expenses.event.dto.CreateEventExpenseRequest;
import com.juntas.expenses.event.dto.EventExpenseResponse;
import com.juntas.expenses.event.dto.UpdateEventExpenseRequest;
import com.juntas.expenses.event.service.EventExpenseService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/event-expenses")
@RequiredArgsConstructor
public class EventExpenseController {
    private final EventExpenseService service;

    @PostMapping
    public ResponseEntity<EventExpenseResponse> create(
            @Valid @RequestBody CreateEventExpenseRequest req) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(req));
    }

    @GetMapping("/{id}")
    public EventExpenseResponse getById(@PathVariable Long id) {
        return service.findById(id);
    }

    @GetMapping
    public List<EventExpenseResponse> getAll() {
        return service.findAll();
    }

    @GetMapping("/neighborhood/{neighborhoodId}")
    public List<EventExpenseResponse> getByNeighborhood(@PathVariable Long neighborhoodId) {
        return service.findByNeighborhood(neighborhoodId);
    }

    @GetMapping("/user/{userId}")
    public List<EventExpenseResponse> getByUser(@PathVariable Long userId) {
        return service.findByUser(userId);
    }

    @PutMapping("/{id}")
    public EventExpenseResponse update(@PathVariable Long id,
                                       @Valid @RequestBody UpdateEventExpenseRequest req) {
        return service.update(id, req);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}