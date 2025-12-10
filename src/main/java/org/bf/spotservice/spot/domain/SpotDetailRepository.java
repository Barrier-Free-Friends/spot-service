package org.bf.spotservice.spot.domain;

import org.bf.spotservice.collection.domain.dto.SpotDto;


public interface SpotDetailRepository {

    SpotDto findByLatitudeAndLongitude(double latitude, double longitude);
}
