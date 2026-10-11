package com.juntas.neighborhood.mapper;

import com.juntas.neighborhood.dto.*;
import com.juntas.neighborhood.model.NeighborhoodModel;
import org.springframework.stereotype.Component;

@Component
public class NeighborhoodMapper {

    public NeighborhoodResponse toResponse(NeighborhoodModel entity) {
        return new NeighborhoodResponse(
                entity.getId(),
                entity.getNeighborhoodName(),
                entity.getPopulationNumber()
        );
    }

    public NeighborhoodModel toEntity(CreateNeighborhoodRequest req) {
        NeighborhoodModel n = new NeighborhoodModel();
        n.setNeighborhoodName(req.neighborhoodName());
        n.setPopulationNumber(req.populationNumber());
        return n;
    }

    public void applyUpdate(UpdateNeighborhoodRequest req, NeighborhoodModel target) {
        if (req.neighborhoodName() != null) {
            target.setNeighborhoodName(req.neighborhoodName());
        }
        if (req.populationNumber() != null) {
            target.setPopulationNumber(req.populationNumber());
        }
    }
}