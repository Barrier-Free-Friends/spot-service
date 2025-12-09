package org.bf.spotservice.collection.domain;

import org.bf.spotservice.collection.domain.dto.CollectionDto;

import java.util.List;

public interface CollectionDetailRepository {
    List<CollectionDto> findAll();
//    CollectionDto findById(Long id);
}
