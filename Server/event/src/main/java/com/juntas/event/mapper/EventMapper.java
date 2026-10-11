package com.juntas.event.mapper;

import com.juntas.event.dto.CreateEventRequest;
import com.juntas.event.dto.EventResponse;
import com.juntas.event.dto.UpdateEventRequest;
import com.juntas.event.model.EventModel;
import org.springframework.stereotype.Component;

@Component
public class EventMapper {
    public EventResponse toResponse(EventModel entity) {
        return new EventResponse(
                entity.getId(),
                entity.getTitle(),
                entity.getDescription(),
                entity.getEventType(),
                entity.getVenueId(),
                entity.getNeighborId(),
                entity.getDate(),
                entity.getStartTime(),
                entity.getEndTime() 
        );
    }

    public EventModel toEntity(CreateEventRequest req) {
        EventModel e = new EventModel();
        e.setTitle(req.title());
        e.setDescription(req.description());
        e.setEventType(req.eventType());
        e.setVenueId(req.venueId());
        e.setDate(req.date());
        e.setStartTime(req.startTime());
        e.setEndTime(req.endTime());
        // neighborId comes via JWT
        return e;
    }

    public void applyUpdate(UpdateEventRequest req, EventModel target) {
        if (req.title() != null) target.setTitle(req.title());
        if (req.description() != null) target.setDescription(req.description());
        if (req.eventType() != null) target.setEventType(req.eventType());
        if (req.venueId() != null) target.setVenueId(req.venueId());
        if (req.date() != null) target.setDate(req.date());
        if (req.startTime() != null) target.setStartTime(req.startTime());
        if (req.endTime() != null) target.setEndTime(req.endTime());
    }
}