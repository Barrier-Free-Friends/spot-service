package org.bf.spotservice.collection.domain.dto;

import java.util.List;

public record CollectionDto(
        Long id,
        List<SpotIdDto> spots
) {}
