package org.bf.spotservice.spot.application;

import org.bf.spotservice.collection.domain.dto.SpotDto;
import org.bf.spotservice.spot.application.dto.request.SpotRequest;
import org.bf.spotservice.spot.domain.SpotRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;


@SpringBootTest
@Transactional
@ActiveProfiles("test")
class SpotServiceTest {

    @Autowired
    private SpotService spotService;

    @Autowired
    private SpotRepository spotRepository;


    @Test
    @DisplayName("DB에 spot이 없으면 새로 저장 후 DTO 반환")
    void createSpot() {

        SpotRequest.createSpotRequest request = new SpotRequest.createSpotRequest(
                "테스트 이름",
                "테스트 주소,",
                37.123456,
                127.123456
        );

        SpotDto spot = spotService.createSpot(request);

        assertThat(spot).isNotNull();
        assertThat(spot.id()).isNotNull();
        assertThat(spot.name()).isEqualTo("테스트 이름");

        assertThat(spot.latitude()).isEqualTo(37.123456);
    }
}