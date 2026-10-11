package com.juntas.event.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.juntas.event.model.EventModel;

public interface EventRepository extends JpaRepository<EventModel, Long> {
    
}
