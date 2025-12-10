package org.bf.spotservice.collection.application.command;

import org.bf.spotservice.collection.application.command.dto.CreateDto;
import org.bf.spotservice.collection.application.command.dto.UpdateDto;
import org.bf.spotservice.collection.domain.dto.CollectionIdDto;

public interface CollectionCommandService {

    // 컬렉션 공개 여부 및 이름 변경
    void updateOpenCollection(Long id, UpdateDto dto);

    // 컬렉션 생성
    CollectionIdDto createCollection(CreateDto dto);

    // 컬렉션 삭제
    void deleteCollection(Long collectionId);

    // 컬렉션에 장소 저장
    CollectionIdDto addSpotIdToCollection(Long collectionId, Long spotId);

    // 컬렉션에서 장소 삭제
    CollectionIdDto removeSpotIdFromCollection(Long collectionId, Long spotId);

}
