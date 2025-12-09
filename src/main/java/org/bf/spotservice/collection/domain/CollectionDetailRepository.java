package org.bf.spotservice.collection.domain;

import org.bf.spotservice.collection.domain.dto.CollectionIdDto;

import java.util.List;

public interface CollectionDetailRepository {
    List<CollectionIdDto> findAll();

}
