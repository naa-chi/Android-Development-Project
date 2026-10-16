package com.juntas.expenses.utility.service;

import com.juntas.expenses.exception.UtilityExpenseNotFoundException;
import com.juntas.expenses.utility.dto.CreateUtilityExpenseRequest;
import com.juntas.expenses.utility.dto.UpdateUtilityExpenseRequest;
import com.juntas.expenses.utility.dto.UtilityExpenseResponse;
import com.juntas.expenses.utility.mapper.UtilityExpenseMapper;
import com.juntas.expenses.utility.model.UtilityExpenseModel;
import com.juntas.expenses.utility.repository.UtilityExpenseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class UtilityExpenseService {
    private final UtilityExpenseRepository repo;
    private final UtilityExpenseMapper mapper;

    public UtilityExpenseResponse create(CreateUtilityExpenseRequest req) {
        return mapper.toResponse(repo.save(mapper.toEntity(req)));
    }

    @Transactional(readOnly = true)
    public UtilityExpenseResponse findById(Long id) {
        return repo.findById(id)
                .map(mapper::toResponse)
                .orElseThrow(() -> new UtilityExpenseNotFoundException(id));
    }

    @Transactional(readOnly = true)
    public List<UtilityExpenseResponse> findAll() {
        return repo.findAll().stream().map(mapper::toResponse).toList();
    }

    @Transactional(readOnly = true)
    public List<UtilityExpenseResponse> findByNeighborhood(Long neighborhoodId) {
        return repo.findAll().stream()
                .filter(e -> neighborhoodId.equals(e.getNeighborhoodId()))
                .map(mapper::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<UtilityExpenseResponse> findByUser(Long forUserId) {
        return repo.findAll().stream()
                .filter(e -> forUserId.equals(e.getForUserId()))
                .map(mapper::toResponse)
                .toList();
    }

    public UtilityExpenseResponse update(Long id, UpdateUtilityExpenseRequest req) {
        UtilityExpenseModel existing = repo.findById(id)
                .orElseThrow(() -> new UtilityExpenseNotFoundException(id));
        mapper.applyUpdate(req, existing);
        return mapper.toResponse(repo.save(existing));
    }

    public void delete(Long id) {
        if (!repo.existsById(id)) {
            throw new UtilityExpenseNotFoundException(id);
        }
        repo.deleteById(id);
    }
}