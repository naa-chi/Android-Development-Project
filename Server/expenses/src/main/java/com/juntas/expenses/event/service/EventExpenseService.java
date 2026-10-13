package com.juntas.expenses.event.service;

import com.juntas.expenses.event.dto.CreateEventExpenseRequest;
import com.juntas.expenses.event.dto.EventExpenseResponse;
import com.juntas.expenses.event.dto.UpdateEventExpenseRequest;
import com.juntas.expenses.event.exception.EventExpenseNotFoundException;
import com.juntas.expenses.event.mapper.EventExpenseMapper;
import com.juntas.expenses.event.model.EventExpenseModel;
import com.juntas.expenses.event.repository.EventExpenseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class EventExpenseService {

    private final EventExpenseRepository repo;
    private final EventExpenseMapper mapper;

    public EventExpenseResponse create(CreateEventExpenseRequest req) {
        return mapper.toResponse(repo.save(mapper.toEntity(req)));
    }

    @Transactional(readOnly = true)
    public EventExpenseResponse findById(Long id) {
        return repo.findById(id)
                .map(mapper::toResponse)
                .orElseThrow(() -> new EventExpenseNotFoundException(id));
    }

    @Transactional(readOnly = true)
    public List<EventExpenseResponse> findAll() {
        return repo.findAll().stream().map(mapper::toResponse).toList();
    }

    @Transactional(readOnly = true)
    public List<EventExpenseResponse> findByNeighborhood(Long neighborhoodId) {
        return repo.findAll().stream()
                .filter(e -> neighborhoodId.equals(e.getNeighborhoodId()))
                .map(mapper::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<EventExpenseResponse> findByUser(Long forUserId) {
        return repo.findAll().stream()
                .filter(e -> forUserId.equals(e.getForUserId()))
                .map(mapper::toResponse)
                .toList();
    }

    public EventExpenseResponse update(Long id, UpdateEventExpenseRequest req) {
        EventExpenseModel existing = repo.findById(id)
                .orElseThrow(() -> new EventExpenseNotFoundException(id));
        mapper.applyUpdate(req, existing);
        return mapper.toResponse(repo.save(existing));
    }

    public void delete(Long id) {
        if (!repo.existsById(id)) {
            throw new EventExpenseNotFoundException(id);
        }
        repo.deleteById(id);
    }
}