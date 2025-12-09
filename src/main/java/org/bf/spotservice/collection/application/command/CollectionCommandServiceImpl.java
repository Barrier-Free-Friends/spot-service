package org.bf.spotservice.collection.application.command;

import lombok.RequiredArgsConstructor;
import org.bf.global.infrastructure.exception.CustomException;
import org.bf.spotservice.collection.application.command.dto.CreateDto;
import org.bf.spotservice.collection.application.command.dto.UpdateDto;
import org.bf.spotservice.collection.application.error.CollectionErrorCode;
import org.bf.spotservice.collection.domain.Collection;
import org.bf.spotservice.collection.domain.CollectionRepository;
import org.bf.spotservice.collection.domain.dto.CollectionIdDto;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class CollectionCommandServiceImpl implements CollectionCommandService {

    private final CollectionRepository collectionRepository;

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
}
