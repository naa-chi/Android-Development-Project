package com.juntas.venue.client;

import com.juntas.venue.client.dto.NeighborhoodDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "neighborhood", path = "/api/v1/neighborhood")
public interface NeighborhoodClient {

    @GetMapping("/{id}")
    NeighborhoodDto getById(@PathVariable("id") Long id);
}