package org.bf.spotservice.collection.application.query;

import org.bf.global.infrastructure.exception.CustomException;
import org.bf.spotservice.collection.domain.CollectionDetailRepository;
import org.bf.spotservice.collection.domain.CollectionRepository;
import org.bf.spotservice.collection.domain.dto.CollectionDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
class CollectionQueryServiceImplTest {

    @Mock
    private CollectionRepository collectionRepository;

    @Mock
    private CollectionDetailRepository collectionDetailRepository;

    private CollectionQueryServiceImpl collectionQueryServiceImpl;
    @BeforeEach
    void setUp() {
        collectionQueryServiceImpl = new CollectionQueryServiceImpl(collectionRepository, collectionDetailRepository);
    }

    @Test
    void 갖고_있는_컬렉션_조회_비어있지_않다() {

        List<CollectionDto> dtoList = List.of(mock(CollectionDto.class));
        when(collectionDetailRepository.findAll()).thenReturn(dtoList);

        List<CollectionDto> collections = collectionQueryServiceImpl.getCollections();


        assertThat(collections).isEqualTo(dtoList);

        // 메서드가 정확히 한 번 호출되었는지 검증
        verify(collectionDetailRepository, times(1)).findAll();
    }

    @Test
    void 갖고_있는_컬렉션이_없는_경우() {
        when(collectionDetailRepository.findAll()).thenReturn(List.of());

        CustomException e = assertThrows(CustomException.class, () -> collectionQueryServiceImpl.getCollections());
        assertNotNull(e);
        verify(collectionDetailRepository, times(1)).findAll();
    }
}