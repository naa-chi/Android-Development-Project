package com.juntas.expenses.utility.controller;

import com.juntas.expenses.utility.dto.CreateUtilityExpenseRequest;
import com.juntas.expenses.utility.dto.UpdateUtilityExpenseRequest;
import com.juntas.expenses.utility.dto.UtilityExpenseResponse;
import com.juntas.expenses.utility.service.UtilityExpenseService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/utilityexpense")
@RequiredArgsConstructor
public class UtilityExpenseController {
    private final UtilityExpenseService service;

    @PostMapping
    public ResponseEntity<UtilityExpenseResponse> create(
            @Valid @RequestBody CreateUtilityExpenseRequest req) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(req));
    }

    @GetMapping("/{id}")
    public UtilityExpenseResponse getById(@PathVariable Long id) {
        return service.findById(id);
    }

    @GetMapping
    public List<UtilityExpenseResponse> getAll() {
        return service.findAll();
    }

    @GetMapping("/by-neighborhood/{neighborhoodId}")
    public List<UtilityExpenseResponse> getByNeighborhood(@PathVariable Long neighborhoodId) {
        return service.findByNeighborhood(neighborhoodId);
    }

    @GetMapping("/by-user/{userId}")
    public List<UtilityExpenseResponse> getByUser(@PathVariable Long userId) {
        return service.findByUser(userId);
    }

    @PutMapping("/{id}")
    public UtilityExpenseResponse update(@PathVariable Long id,
                                         @Valid @RequestBody UpdateUtilityExpenseRequest req) {
        return service.update(id, req);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}