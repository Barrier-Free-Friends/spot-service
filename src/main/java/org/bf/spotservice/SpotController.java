package org.bf.spotservice;

import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.bf.global.infrastructure.CustomResponse;
import org.bf.global.infrastructure.success.GeneralSuccessCode;
import org.bf.spotservice.collection.domain.dto.SpotDto;
import org.bf.spotservice.spot.application.SpotService;
import org.bf.spotservice.spot.application.dto.request.SpotRequest;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Tag(name = "Spot Controller", description = "Spot 관련 API")
public class SpotController {

    private final SpotService spotService;

    @PostMapping("/spots")
    public CustomResponse<SpotDto> createSpot(@RequestBody SpotRequest.createSpotRequest request) {
        SpotDto spotDto = spotService.createSpot(request);
        return CustomResponse.onSuccess(GeneralSuccessCode.CREATED, spotDto);
    }
}
