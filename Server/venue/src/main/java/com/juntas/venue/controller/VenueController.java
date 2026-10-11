package com.juntas.venue.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.juntas.venue.dto.CreateVenueRequest;
import com.juntas.venue.dto.UpdateVenueRequest;
import com.juntas.venue.dto.VenueResponse;
import com.juntas.venue.service.VenueService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/venues")
@RequiredArgsConstructor
public class VenueController {

    private final VenueService venueService;

    @PostMapping
    public ResponseEntity<VenueResponse> create(@Valid @RequestBody CreateVenueRequest req) {
        return ResponseEntity.status(HttpStatus.CREATED).body(venueService.create(req));
    }

    @GetMapping("/{id}")
    public VenueResponse getById(@PathVariable Long id) {
        return venueService.findById(id);
    }

    @GetMapping
    public List<VenueResponse> getAll() {
        return venueService.findAll();
    }

    @PutMapping("/{id}")
    public VenueResponse update(@PathVariable Long id, @Valid @RequestBody UpdateVenueRequest req) {
        return venueService.update(id, req);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        venueService.delete(id);
        return ResponseEntity.noContent().build();
    }
}