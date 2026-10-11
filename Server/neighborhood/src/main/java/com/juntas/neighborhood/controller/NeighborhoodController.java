package com.juntas.neighborhood.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.juntas.neighborhood.dto.CreateNeighborhoodRequest;
import com.juntas.neighborhood.dto.UpdateNeighborhoodRequest;
import com.juntas.neighborhood.dto.NeighborhoodResponse;
import com.juntas.neighborhood.service.NeighborhoodService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/neighborhoods")
@RequiredArgsConstructor
public class NeighborhoodController {

    private final NeighborhoodService neighborhoodService;

    @PostMapping
    public ResponseEntity<NeighborhoodResponse> create(@Valid @RequestBody CreateNeighborhoodRequest req) {
        return ResponseEntity.status(HttpStatus.CREATED).body(neighborhoodService.create(req));
    }

    @GetMapping("/{id}")
    public NeighborhoodResponse getById(@PathVariable Long id) {
        return neighborhoodService.findById(id);
    }

    @GetMapping
    public List<NeighborhoodResponse> getAll() {
        return neighborhoodService.findAll();
    }

    @PutMapping("/{id}")
    public NeighborhoodResponse update(@PathVariable Long id, @Valid @RequestBody UpdateNeighborhoodRequest req) {
        return neighborhoodService.update(id, req);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        neighborhoodService.delete(id);
        return ResponseEntity.noContent().build();
    }
}