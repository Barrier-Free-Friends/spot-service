package org.bf.spotservice.spot.application.dto.request;

public record SpotRequest() {

    public record createSpotRequest(
            String name,
            String address,
            double latitude,
            double longitude
    ) {}

    public record spotLocationDto(
            double latitude,
            double longitude
    ) {}
}
