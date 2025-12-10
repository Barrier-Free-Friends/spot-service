package org.bf.spotservice.collection.application.dto;

import org.bf.spotservice.collection.domain.Collection;
import org.bf.spotservice.collection.domain.dto.SpotIdDto;

import java.util.List;

public record CollectionRankDto(
        Long id,
        String name,
        Boolean open,
        int fork,
        List<SpotIdDto> spots
) {

    public static CollectionRankDto from(Collection collection) {
        return new CollectionRankDto(
                collection.getId(),
                collection.getName(),
                collection.getOpen(),
                collection.getFork(),
                collection.getSpotIds() == null ? List.of() : collection.getSpotIds().stream().map(SpotIdDto::new).toList()
        );
    }

}
