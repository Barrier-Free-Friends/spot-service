package org.bf.spotservice.collection.application.query;

import org.bf.global.infrastructure.exception.CustomException;
import org.bf.global.security.SecurityUtils;
import org.bf.spotservice.collection.application.dto.CollectionRankDto;
import org.bf.spotservice.collection.domain.Collection;
import org.bf.spotservice.collection.domain.CollectionDetailRepository;
import org.bf.spotservice.collection.domain.CollectionRepository;
import org.bf.spotservice.collection.domain.dto.CollectionDto;
import org.bf.spotservice.collection.domain.dto.CollectionIdDto;
import org.bf.spotservice.spot.domain.Spot;
import org.bf.spotservice.spot.domain.SpotRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;
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

    @Mock
    private SecurityUtils securityUtils;

    @InjectMocks
    private CollectionQueryServiceImpl collectionQueryServiceImpl;


    @BeforeEach
    void setUp() {
        collectionQueryServiceImpl = new CollectionQueryServiceImpl(collectionRepository, collectionDetailRepository, securityUtils, spotRepository);
    }

    @Test
    void 갖고_있는_컬렉션_조회_비어있지_않다() {

        UUID userId = collectionRepository.findById(1L).orElseThrow().getUserId();

        List<CollectionIdDto> dtoList = List.of(mock(CollectionIdDto.class));
        when(collectionDetailRepository.findAll(userId)).thenReturn(dtoList);

        List<CollectionIdDto> collections = collectionQueryServiceImpl.getCollections();


        assertThat(collections).isEqualTo(dtoList);

        // 메서드가 정확히 한 번 호출되었는지 검증
        verify(collectionDetailRepository, times(1)).findAll(userId);
    }

    @Test
    void 갖고_있는_컬렉션이_없는_경우() {
        UUID userId = collectionRepository.findById(1L).orElseThrow().getUserId();

        when(collectionDetailRepository.findAll(userId)).thenReturn(List.of());

        CustomException e = assertThrows(CustomException.class, () -> collectionQueryServiceImpl.getCollections());
        assertNotNull(e);
        verify(collectionDetailRepository, times(1)).findAll(userId);
    }


    @Test
    void 특정_컬렉션_상세_조회() {
        Long collectionId = 1L;
        UUID userId = collectionRepository.findById(1L).orElseThrow().getUserId();

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

        CollectionDto collectionDto = collectionQueryServiceImpl.getCollection(collectionId, userId);

        assertThat(collectionDto.id()).isEqualTo(collectionId);
        assertThat(collectionDto.spots()).hasSize(1);
        assertThat(collectionDto.spots().getFirst().id()).isEqualTo(10L);
    }

    @Test
    @DisplayName("Fork 기반 내림차순 랭킹 조회")
    void fork_기반_컬렉션_랭킹_조회() {


        Pageable pageable = PageRequest.of(0, 10);

        CollectionRankDto rank1 = new CollectionRankDto(1L, "인기 컬렉션", true, 100, List.of());
        CollectionRankDto rank2 = new CollectionRankDto(2L, "보통 컬렉션", true, 50, List.of());
        CollectionRankDto rank3 = new CollectionRankDto(3L, "신규 컬렉션", true, 10, List.of());

        List<CollectionRankDto> ranks = List.of(rank1, rank2, rank3);
        Page<CollectionRankDto> expectedPage = new PageImpl<>(ranks, pageable, ranks.size());

        // 레포지토리 동작
        given(collectionDetailRepository.findCollectionByForkDesc(pageable)).willReturn(expectedPage);

        Page<CollectionRankDto> result = collectionQueryServiceImpl.getCollectionsByFork(pageable);

        assertThat(result).isNotNull();
        assertThat(result.getContent()).hasSize(3);

        assertThat(result.getContent().get(0).fork()).isEqualTo(100);
    }
}