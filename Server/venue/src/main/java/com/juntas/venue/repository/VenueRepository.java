package com.juntas.venue.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.juntas.venue.model.VenueModel;

public interface VenueRepository extends JpaRepository<VenueModel, Long> {

}
