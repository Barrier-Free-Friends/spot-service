package org.bf.spotservice.collection.domain.dto;

import lombok.Builder;

import java.util.List;

@Builder
public record CollectionDto(
        Long id,
        List<SpotDto> spots

) {
}
