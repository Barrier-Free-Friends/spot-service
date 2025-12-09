package org.bf.spotservice.collection.domain.dto;

import java.util.List;

public record CollectionIdDto(
        Long id,
        List<SpotIdDto> spots
) {}
