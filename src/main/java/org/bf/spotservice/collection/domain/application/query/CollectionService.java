package org.bf.spotservice.collection.domain.application.query;

import lombok.RequiredArgsConstructor;
import org.bf.spotservice.collection.domain.dto.CollectionDto;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CollectionService implements CollectionQueryService {


    @Override
    public List<CollectionDto> getCollections() {
        return null;
    }
}
