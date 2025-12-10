package org.bf.spotservice.spot.application;

import lombok.RequiredArgsConstructor;
import org.bf.spotservice.collection.domain.dto.SpotDto;
import org.bf.spotservice.spot.domain.Spot;
import org.bf.spotservice.spot.domain.SpotDetailRepository;
import org.bf.spotservice.spot.domain.SpotRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static org.bf.spotservice.spot.application.dto.request.SpotRequest.createSpotRequest;

@Service
@RequiredArgsConstructor
public class SpotService {

    private final SpotRepository spotRepository;
    private final SpotDetailRepository spotDetailRepository;

    /**
     * @param request
     * @return SpotDto
     * 여러 사용자가 같은 장소를 동시에 생성하는 상황을 고려하여, 이미 존재하는 장소가 있다면 해당 장소를 반환하고,
     * 존재하지 않는다면 새로운 장소를 생성하여 반환합니다.
     */
    @Transactional
     public SpotDto createSpot(createSpotRequest request) {

        SpotDto exists = spotDetailRepository.findByLatitudeAndLongitude(request.latitude(), request.longitude());

        if (exists != null) return exists;

        Spot newSpot = Spot.builder()
                .name(request.name())
                .address(request.address())
                .latitude(request.latitude())
                .longitude(request.longitude())
                .build();

        Spot saved = spotRepository.save(newSpot);

        return new SpotDto(
                saved.getId(),
                saved.getName(),
                saved.getAddress(),
                saved.getLatitude(),
                saved.getLongitude()
        );
    }

}
