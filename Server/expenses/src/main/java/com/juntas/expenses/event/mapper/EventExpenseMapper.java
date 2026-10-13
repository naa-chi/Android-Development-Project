package com.juntas.expenses.event.mapper;

import com.juntas.expenses.event.dto.CreateEventExpenseRequest;
import com.juntas.expenses.event.dto.EventExpenseResponse;
import com.juntas.expenses.event.dto.UpdateEventExpenseRequest;
import com.juntas.expenses.event.model.EventExpenseModel;
import org.springframework.stereotype.Component;

@Component
public class EventExpenseMapper {

    public EventExpenseResponse toResponse(EventExpenseModel entity) {
        return new EventExpenseResponse(
                entity.getId(),
                entity.getEmissionDate(),
                entity.getBill(),
                entity.getForUserId(),
                entity.getForEmail(),
                entity.getNeighborhoodId()
        );
    }

    public EventExpenseModel toEntity(CreateEventExpenseRequest req) {
        EventExpenseModel e = new EventExpenseModel();
        e.setEmissionDate(req.emissionDate());
        e.setBill(req.bill());
        e.setForUserId(req.forUserId());
        e.setForEmail(req.forEmail());
        e.setNeighborhoodId(req.neighborhoodId());
        return e;
    }

    public void applyUpdate(UpdateEventExpenseRequest req, EventExpenseModel target) {
        if (req.emissionDate() != null) target.setEmissionDate(req.emissionDate());
        if (req.bill() != null) target.setBill(req.bill());
        if (req.forUserId() != null) target.setForUserId(req.forUserId());
        if (req.forEmail() != null) target.setForEmail(req.forEmail());
        if (req.neighborhoodId() != null) target.setNeighborhoodId(req.neighborhoodId());
    }
}