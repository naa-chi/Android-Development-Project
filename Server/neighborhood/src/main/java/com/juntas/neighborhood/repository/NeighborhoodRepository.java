package com.juntas.neighborhood.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.juntas.neighborhood.model.NeighborhoodModel;

public interface NeighborhoodRepository extends JpaRepository<NeighborhoodModel, Long> {
    
}
