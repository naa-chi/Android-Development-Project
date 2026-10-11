package com.juntas.venue.mapper;

import com.juntas.venue.dto.CreateVenueRequest;
import com.juntas.venue.dto.UpdateVenueRequest;
import com.juntas.venue.dto.VenueResponse;
import com.juntas.venue.model.VenueModel;
import org.springframework.stereotype.Component;

@Component
public class VenueMapper {
    public VenueResponse toResponse(VenueModel entity) {
        return new VenueResponse(
                entity.getId(),
                entity.getVenueName(),
                entity.getType(),
                entity.getPricePerHour(),
                entity.getCapacity(),
                entity.getNeighbourhoodId()
        );
    }

    public VenueModel toEntity(CreateVenueRequest req) {
        VenueModel v = new VenueModel();
        v.setVenueName(req.name());
        v.setType(req.type());
        v.setPricePerHour(req.pricePerHour());
        v.setCapacity(req.capacity());
        v.setNeighbourhoodId(req.neighbourhoodId());
        return v;
    }

    public void applyUpdate(UpdateVenueRequest req, VenueModel target) {
        if (req.name() != null) target.setVenueName(req.name());
        if (req.type() != null) target.setType(req.type());
        if (req.pricePerHour() != null) target.setPricePerHour(req.pricePerHour());
        if (req.capacity() != null) target.setCapacity(req.capacity());
        if (req.neighbourhoodId() != null) target.setNeighbourhoodId(req.neighbourhoodId());
    }
}