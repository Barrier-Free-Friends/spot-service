package org.bf.spotservice.collection.application.query;

import org.bf.spotservice.collection.application.dto.CollectionRankDto;
import org.bf.spotservice.collection.domain.dto.CollectionDto;
import org.bf.spotservice.collection.domain.dto.CollectionIdDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CollectionQueryService {

    // 갖고 있는 컬렉션 조회
    List<CollectionIdDto> getCollections();

    // 특정 컬렉션 조회
    CollectionDto getCollection(Long id);

    // 컬렉션 랭킹 조회
    Page<CollectionRankDto> getCollectionsByFork(Pageable pageable);
}
