package org.bf.spotservice.collection.domain;

import org.bf.spotservice.collection.application.dto.CollectionRankDto;
import org.bf.spotservice.collection.domain.dto.CollectionIdDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.UUID;

public interface CollectionDetailRepository {
    List<CollectionIdDto> findAll(UUID currentUserId);

    Page<CollectionRankDto> findCollectionByForkDesc(Pageable pageable);

}
