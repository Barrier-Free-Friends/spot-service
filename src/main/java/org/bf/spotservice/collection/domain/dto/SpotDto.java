package org.bf.spotservice.collection.domain.dto;


public record SpotDto(
        Long id,
        String name,
        String address,
        double latitude,
        double longitude
) {
}
