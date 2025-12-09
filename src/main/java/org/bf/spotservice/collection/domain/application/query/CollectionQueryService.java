package org.bf.spotservice.collection.domain.application.query;

import org.bf.spotservice.collection.domain.dto.CollectionDto;

import java.util.List;

public interface CollectionQueryService {

    // 갖고 있는 컬렉션 조회
    List<CollectionDto> getCollections();
}
