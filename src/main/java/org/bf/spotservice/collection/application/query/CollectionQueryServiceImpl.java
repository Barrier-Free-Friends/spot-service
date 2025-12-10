package org.bf.spotservice.collection.application.query;

import lombok.RequiredArgsConstructor;
import org.bf.global.infrastructure.exception.CustomException;
import org.bf.spotservice.collection.application.dto.CollectionRankDto;
import org.bf.spotservice.collection.application.error.CollectionErrorCode;
import org.bf.spotservice.collection.domain.Collection;
import org.bf.spotservice.collection.domain.CollectionDetailRepository;
import org.bf.spotservice.collection.domain.CollectionRepository;
import org.bf.spotservice.collection.domain.dto.CollectionDto;
import org.bf.spotservice.collection.domain.dto.CollectionIdDto;
import org.bf.spotservice.collection.domain.dto.SpotDto;
import org.bf.spotservice.spot.domain.Spot;
import org.bf.spotservice.spot.domain.SpotRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CollectionQueryServiceImpl implements CollectionQueryService {

    private final CollectionRepository collectionRepository;
    private final CollectionDetailRepository collectionDetailRepository;

    private final SpotRepository spotRepository;


    // 갖고 있는 컬렉션 조회
    @Override
    public List<CollectionIdDto> getCollections() {

        List<CollectionIdDto> collections = collectionDetailRepository.findAll();

        if (collections.isEmpty()) {
            throw  new CustomException(CollectionErrorCode.COLLECTION_EMPTY);
        }
        return collections;
    }

    // 특정 컬렉션 상세 조회
    @Override
    public CollectionDto getCollection(Long id) {

        Collection collection = collectionRepository.findById(id).orElseThrow((() -> new CustomException(CollectionErrorCode.COLLECTION_NOT_FOUND)));

        // Spot 목록을 가져온다
        List<Spot> spots = spotRepository.findAllById(collection.getSpotIds());
        List<SpotDto> spotDtoList = spots.stream().map(spot -> new SpotDto(
                spot.getId(),
                spot.getName(),
                spot.getAddress(),
                spot.getLatitude(),
                spot.getLongitude())).toList();

        return CollectionDto.builder()
                .id(collection.getId())
                .spots(spotDtoList)
                .build();

    }

    // fork 기준 컬렉션 랭킹 조회
    @Override
    public Page<CollectionRankDto> getCollectionsByFork(Pageable pageable) {

        return collectionDetailRepository.findCollectionByForkDesc(pageable);
    }
}
