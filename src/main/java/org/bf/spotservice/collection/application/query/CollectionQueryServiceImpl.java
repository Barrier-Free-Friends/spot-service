package org.bf.spotservice.collection.application.query;

import lombok.RequiredArgsConstructor;
import org.bf.global.infrastructure.exception.CustomException;
import org.bf.spotservice.collection.application.error.CollectionErrorCode;
import org.bf.spotservice.collection.domain.CollectionDetailRepository;
import org.bf.spotservice.collection.domain.CollectionRepository;
import org.bf.spotservice.collection.domain.dto.CollectionDto;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CollectionQueryServiceImpl implements CollectionQueryService {

    private final CollectionRepository collectionRepository;
    private final CollectionDetailRepository collectionDetailRepository;


    // 갖고 있는 컬렉션 조회
    @Override
    public List<CollectionDto> getCollections() {

        List<CollectionDto> collections = collectionDetailRepository.findAll();

        if (collections.isEmpty()) {
            throw  new CustomException(CollectionErrorCode.COLLECTION_EMPTY);
        }
        return collections;
    }
}
