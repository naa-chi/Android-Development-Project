package com.juntas.venue.service;

import com.juntas.venue.client.NeighborhoodClient;
import com.juntas.venue.dto.CreateVenueRequest;
import com.juntas.venue.dto.UpdateVenueRequest;
import com.juntas.venue.dto.VenueResponse;
import com.juntas.venue.exception.NeighborhoodServiceUnavailableException;
import com.juntas.venue.exception.VenueNotFoundException;
import com.juntas.venue.mapper.VenueMapper;
import com.juntas.venue.model.VenueModel;
import com.juntas.venue.repository.VenueRepository;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
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
    private final NeighborhoodClient neighborhoodClient;
    
    @CircuitBreaker(name = "neighborhoods", fallbackMethod = "createFallback")
    public VenueResponse create(CreateVenueRequest req) {
        neighborhoodClient.getById(req.neighbourhoodId());

        VenueModel saved = venueRepo.save(mapper.toEntity(req));
        return mapper.toResponse(saved);
    }

    @Transactional(readOnly = true)
    public VenueResponse findById(Long id) {
        return venueRepo.findById(id)
                .map(mapper::toResponse)
                .orElseThrow(() -> new VenueNotFoundException(id));
    }

    @Transactional(readOnly = true)
    public List<VenueResponse> findAll() {
        return venueRepo.findAll().stream()
                .map(mapper::toResponse)
                .toList();
    }

    @CircuitBreaker(name = "neighborhoods", fallbackMethod = "updateFallback")
    public VenueResponse update(Long id, UpdateVenueRequest req) {
        VenueModel existing = venueRepo.findById(id)
                .orElseThrow(() -> new VenueNotFoundException(id));
        if (req.neighbourhoodId() != null
                && !req.neighbourhoodId().equals(existing.getNeighborhoodId())) {
            neighborhoodClient.getById(req.neighbourhoodId());
        }

        mapper.applyUpdate(req, existing);
        return mapper.toResponse(venueRepo.save(existing));
    }

    public void delete(Long id) {
        if (!venueRepo.existsById(id)) {
            throw new VenueNotFoundException(id);
        }
        venueRepo.deleteById(id);
    }

    private VenueResponse createFallback(CreateVenueRequest req, Throwable t) {
        throw new NeighborhoodServiceUnavailableException(
                "No se pudo verificar el barrio en este momento. Intente nuevamente.");
    }

    private VenueResponse updateFallback(Long id, UpdateVenueRequest req, Throwable t) {
        throw new NeighborhoodServiceUnavailableException(
                "No se pudo verificar el barrio en este momento. Intente nuevamente.");
    }
}