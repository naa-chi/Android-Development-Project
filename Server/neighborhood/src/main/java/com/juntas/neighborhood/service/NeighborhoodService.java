package com.juntas.neighborhood.service;

import com.juntas.neighborhood.dto.*;
import com.juntas.neighborhood.exception.NeighborhoodAlreadyExistsException;
import com.juntas.neighborhood.exception.NeighborhoodNotFoundException;
import com.juntas.neighborhood.mapper.NeighborhoodMapper;
import com.juntas.neighborhood.model.NeighborhoodModel;
import com.juntas.neighborhood.repository.NeighborhoodRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class NeighborhoodService {
    private final NeighborhoodRepository neighborhoodRepo;
    private final NeighborhoodMapper mapper;

    public NeighborhoodResponse create(CreateNeighborhoodRequest req) {
        if (neighborhoodRepo.existsByNeighborhoodName(req.neighborhoodName())) {
            throw new NeighborhoodAlreadyExistsException(req.neighborhoodName());
        }
        NeighborhoodModel saved = neighborhoodRepo.save(mapper.toEntity(req));
        return mapper.toResponse(saved);
    }

    @Transactional(readOnly = true)
    public NeighborhoodResponse findById(Long id) {
        return neighborhoodRepo.findById(id)
                .map(mapper::toResponse)
                .orElseThrow(() -> new NeighborhoodNotFoundException(id));
    }

    @Transactional(readOnly = true)
    public List<NeighborhoodResponse> findAll() {
        return neighborhoodRepo.findAll().stream()
                .map(mapper::toResponse)
                .toList();
    }

    public NeighborhoodResponse update(Long id, UpdateNeighborhoodRequest req) {
        NeighborhoodModel existing = neighborhoodRepo.findById(id)
                .orElseThrow(() -> new NeighborhoodNotFoundException(id));
        mapper.applyUpdate(req, existing);
        return mapper.toResponse(neighborhoodRepo.save(existing));
    }

    public void delete(Long id) {
        if (!neighborhoodRepo.existsById(id)) {
            throw new NeighborhoodNotFoundException(id);
        }
        neighborhoodRepo.deleteById(id);
    }
}