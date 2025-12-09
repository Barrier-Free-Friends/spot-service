package org.bf.spotservice.collection.application.query;

import org.bf.spotservice.collection.domain.dto.CollectionDto;
import org.bf.spotservice.collection.domain.dto.CollectionIdDto;

import java.util.List;

public interface CollectionQueryService {

    // 갖고 있는 컬렉션 조회
    List<CollectionIdDto> getCollections();

    // 특정 컬렉션 조회
    CollectionDto getCollection(Long id);
}
