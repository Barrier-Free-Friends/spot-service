package org.bf.spotservice.collection.application.command;

import lombok.RequiredArgsConstructor;
import org.bf.global.infrastructure.exception.CustomException;
import org.bf.spotservice.collection.application.command.dto.UpdateDto;
import org.bf.spotservice.collection.application.error.CollectionErrorCode;
import org.bf.spotservice.collection.domain.Collection;
import org.bf.spotservice.collection.domain.CollectionRepository;
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
}
