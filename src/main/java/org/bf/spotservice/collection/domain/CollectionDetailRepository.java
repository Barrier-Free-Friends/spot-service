package org.bf.spotservice.collection.domain;

import org.bf.spotservice.collection.application.dto.CollectionRankDto;
import org.bf.spotservice.collection.domain.dto.CollectionDto;
import org.bf.spotservice.collection.domain.dto.CollectionIdDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CollectionDetailRepository {
    List<CollectionIdDto> findAll();

    Page<CollectionRankDto> findCollectionByForkDesc(Pageable pageable);

}
