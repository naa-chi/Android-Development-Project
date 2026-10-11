package com.juntas.venue.client;

import com.juntas.venue.client.dto.NeighborhoodDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "neighborhoods", path = "/api/v1/neighborhoods")
public interface NeighborhoodClient {

    @GetMapping("/{id}")
    NeighborhoodDto getById(@PathVariable("id") Long id);
}