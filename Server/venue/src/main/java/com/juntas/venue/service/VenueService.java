package com.juntas.venue.service;

import com.juntas.venue.dto.*;
import com.juntas.venue.mapper.VenueMapper;
import com.juntas.venue.model.VenueModel;
import com.juntas.venue.repository.VenueRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class VenueService {

    private final VenueRepository venueRepo;
    private final VenueMapper mapper;

    public VenueResponse create(CreateVenueRequest req) {
        VenueModel saved = venueRepo.save(mapper.toEntity(req));
        return mapper.toResponse(saved);
    }

    @Transactional(readOnly = true)
    public VenueResponse findById(Long id) {
        return venueRepo.findById(id)
                .map(mapper::toResponse)
                .orElseThrow(() -> new RuntimeException("Venue hasn't been found: " + id));
    }

    @Transactional(readOnly = true)
    public List<VenueResponse> findAll() {
        return venueRepo.findAll().stream()
                .map(mapper::toResponse)
                .toList();
    }

    public VenueResponse update(Long id, UpdateVenueRequest req) {
        VenueModel existing = venueRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Venue hasn't been found: " + id));
        mapper.applyUpdate(req, existing);
        return mapper.toResponse(venueRepo.save(existing));
    }

    public void delete(Long id) {
        if (!venueRepo.existsById(id)) {
            throw new RuntimeException("Venue hasn't been found: " + id);
        }
        venueRepo.deleteById(id);
    }
}