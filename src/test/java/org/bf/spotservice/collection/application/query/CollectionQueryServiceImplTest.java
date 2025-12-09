package org.bf.spotservice.collection.application.query;

import org.bf.global.infrastructure.exception.CustomException;
import org.bf.spotservice.collection.domain.Collection;
import org.bf.spotservice.collection.domain.CollectionDetailRepository;
import org.bf.spotservice.collection.domain.CollectionRepository;
import org.bf.spotservice.collection.domain.dto.CollectionDto;
import org.bf.spotservice.collection.domain.dto.CollectionIdDto;
import org.bf.spotservice.spot.domain.Spot;
import org.bf.spotservice.spot.domain.SpotRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Optional;

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

    @Mock
    private SpotRepository spotRepository;

    private CollectionQueryServiceImpl collectionQueryServiceImpl;

    @BeforeEach
    void setUp() {
        collectionQueryServiceImpl = new CollectionQueryServiceImpl(collectionRepository, collectionDetailRepository, spotRepository);
    }

    @Test
    void 갖고_있는_컬렉션_조회_비어있지_않다() {

        List<CollectionIdDto> dtoList = List.of(mock(CollectionIdDto.class));
        when(collectionDetailRepository.findAll()).thenReturn(dtoList);

        List<CollectionIdDto> collections = collectionQueryServiceImpl.getCollections();


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


    @Test
    void 특정_컬렉션_상세_조회() {
        Long collectionId = 1L;

        Collection collection = mock(Collection.class);
        when(collectionRepository.findById(collectionId)).thenReturn(Optional.of(collection));
        when(collection.getId()).thenReturn(collectionId);
        when(collection.getSpotIds()).thenReturn(List.of(10L, 20L));

        Spot spot = mock(Spot.class);
        when(spot.getId()).thenReturn(10L);
        when(spot.getName()).thenReturn("spot-1-name");
        when(spot.getAddress()).thenReturn("spot-1-address");
        when(spot.getLatitude()).thenReturn(12.34);
        when(spot.getLongitude()).thenReturn(56.78);

        when(spotRepository.findAllById(List.of(10L, 20L))).thenReturn(List.of(spot));

        CollectionDto collectionDto = collectionQueryServiceImpl.getCollection(collectionId);

        assertThat(collectionDto.id()).isEqualTo(collectionId);
        assertThat(collectionDto.spots()).hasSize(1);
        assertThat(collectionDto.spots().getFirst().id()).isEqualTo(10L);
    }
}