package org.bf.spotservice.collection.application.command;

import lombok.RequiredArgsConstructor;
import org.bf.global.infrastructure.exception.CustomException;
import org.bf.global.security.SecurityUtils;
import org.bf.spotservice.collection.application.command.dto.CreateDto;
import org.bf.spotservice.collection.application.command.dto.UpdateDto;
import org.bf.spotservice.collection.application.error.CollectionErrorCode;
import org.bf.spotservice.collection.domain.Collection;
import org.bf.spotservice.collection.domain.CollectionRepository;
import org.bf.spotservice.collection.domain.dto.CollectionIdDto;
import org.bf.spotservice.spot.application.error.SpotErrorCode;
import org.bf.spotservice.spot.domain.SpotRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class CollectionCommandServiceImpl implements CollectionCommandService {

    private final CollectionRepository collectionRepository;
    private final SecurityUtils securityUtils;
    private final SpotRepository spotRepository;

    @Override
    public void updateOpenCollection(Long id, UpdateDto dto) {

        Collection collection = collectionRepository.findById(id).orElseThrow(() -> new CustomException(CollectionErrorCode.COLLECTION_NOT_FOUND));

        collection.updateOpenAndName(dto);
    }

    @Override
    public CollectionIdDto createCollection(CreateDto dto) {

        // 컬렉션을 먼저 만들고 나중에 스팟을 추가하는 방식
        // 따라서 생성시에는 스팟 아이디가 비어있음
        Collection collection = new Collection(dto.isOpen(), dto.name());

        collectionRepository.save(collection);
        return collection.toDto();

    }

    @Override
    public void deleteCollection(Long collectionId) {

        Collection collection = collectionRepository.findById(collectionId).orElseThrow(() -> new CustomException(CollectionErrorCode.COLLECTION_NOT_FOUND));

        String username = securityUtils.getCurrentUsername();
        collection.deleteCollection(username);
    }

    @Override
    public CollectionIdDto addSpotIdToCollection(Long collectionId, Long spotId) {

        Collection collection = collectionRepository.findById(collectionId).orElseThrow(() -> new CustomException(CollectionErrorCode.COLLECTION_NOT_FOUND));
        spotRepository.findById(spotId).orElseThrow(() -> new CustomException(SpotErrorCode.SPOT_NOT_FOUND));

        collection.addSpotId(spotId);

        return collection.toDto();
    }

    @Override
    public CollectionIdDto removeSpotIdFromCollection(Long collectionId, Long spotId) {

        Collection collection = collectionRepository.findById(collectionId).orElseThrow(() -> new CustomException(CollectionErrorCode.COLLECTION_NOT_FOUND));

        collection.removeSpotId(spotId);

        return collection.toDto();

    }

    @Override
    public CollectionIdDto forkCollection(Long originalCollectionId, UUID userId) {

        Collection originalCollection = collectionRepository.findByCollectionId(originalCollectionId).orElseThrow(() -> new CustomException(CollectionErrorCode.COLLECTION_NOT_FOUND));

        if (Boolean.FALSE.equals(originalCollection.getOpen())) {
          throw new CustomException(CollectionErrorCode.COLLECTION_NOT_PUBLIC);
        }

        originalCollection.incrementFork();

        Collection newCollection = Collection.createForkedCollection(originalCollection, userId);

        Collection savedForkedCollection = collectionRepository.save(newCollection);

        return savedForkedCollection.toDto();
    }
}
