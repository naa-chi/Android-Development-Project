package com.juntas.event.client;

import com.juntas.event.client.dto.VenueDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "venues", path = "/api/v1/venues")
public interface VenueClient {

    @GetMapping("/{id}")
    VenueDto getById(@PathVariable("id") Long id);
}