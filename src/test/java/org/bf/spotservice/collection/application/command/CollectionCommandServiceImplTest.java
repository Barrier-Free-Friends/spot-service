package org.bf.spotservice.collection.application.command;

import jakarta.inject.Inject;
import org.assertj.core.api.Assertions;
import org.bf.global.infrastructure.exception.CustomException;
import org.bf.global.security.SecurityUtils;
import org.bf.spotservice.collection.application.command.dto.CreateDto;
import org.bf.spotservice.collection.domain.Collection;
import org.bf.spotservice.collection.domain.CollectionRepository;
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
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
class CollectionCommandServiceImplTest {

    @InjectMocks
    private CollectionCommandServiceImpl collectionCommandService;

    @Mock
    private CollectionRepository collectionRepository;

    @Mock
    private SpotRepository spotRepository;

    @Test
    @DisplayName("컬렉션 생성 로직 테스트 : 초키 생성 시 스팟ids는 비어있다.")
    void 컬렉션_생성_로직() {

        UUID userId = UUID.randomUUID();
        CreateDto createDto = new CreateDto(true, "test1");

        given(collectionRepository.save(any(Collection.class))).willAnswer(invocation -> {
            Collection collection = invocation.getArgument(0);
            // db 강제 id 세팅
            ReflectionTestUtils.setField(collection, "id", 1L);
            return collection;
        });

        // When
        CollectionIdDto result = collectionCommandService.createCollection(createDto);

        // Then
        assertThat(result.id()).isEqualTo(1L);
        assertThat(result.spots()).isNotNull();      // null이 아니어야 함
        assertThat(result.spots()).isEmpty();        // 초기이므로 비어있어야 함
    }

    @Test
    void 컬랙션에_spotId_추가() {

        Long spotId = 1L;
        Long collectionId = 10L;

        Collection collection = new Collection(UUID.randomUUID(), true, "test collection");

        ReflectionTestUtils.setField(collection, "id", collectionId);

        Spot spot = mock(Spot.class);
        given(collectionRepository.findById(collectionId)).willReturn(Optional.of(collection));
        given(spotRepository.findById(spotId)).willReturn(Optional.of(spot));

        CollectionIdDto result = collectionCommandService.addSpotIdToCollection(collectionId, spotId);

        assertThat(result).isNotNull();
        assertThat(result.spots()).hasSize(1);
        assertThat(result.spots().get(0).id()).isEqualTo(spotId);
    }

    @Test
    @DisplayName("컬렉션 포크 테스트")
    void 컬렉션_fork() {

        Long originalCollectionId = 1L;
        UUID myId = UUID.randomUUID();
        UUID originalOwnerId = UUID.randomUUID();

        Collection originalCollection = new Collection(UUID.randomUUID(), true, "원본 컬렉션");
        ReflectionTestUtils.setField(originalCollection, "id", originalCollectionId);
        originalCollection.addSpotId(100L);
        originalCollection.addSpotId(200L);

        Optional<Collection> collectionId = collectionRepository.findByCollectionId(originalCollectionId);
        given(collectionId).willReturn(Optional.of(originalCollection));

        // 저장 시 새로운 id(2L) 부여
        given(collectionRepository.save(any(Collection.class))).willAnswer(invocation -> {
            Collection c = invocation.getArgument(0);
            ReflectionTestUtils.setField(c, "id", 2L);
            return c;
        });

        // when
        CollectionIdDto result = collectionCommandService.forkCollection(originalCollectionId, myId);

        // then
        assertThat(originalCollection.getFork()).isEqualTo(1);
        assertThat(result.id()).isEqualTo(2L);

        assertThat(result.spots()).hasSize(2);
        assertThat(result.spots()).extracting("id").containsExactly(100L, 200L);

    }
}