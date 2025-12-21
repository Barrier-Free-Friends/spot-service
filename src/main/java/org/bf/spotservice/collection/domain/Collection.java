package org.bf.spotservice.collection.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.bf.global.domain.Auditable;
import org.bf.global.infrastructure.exception.CustomException;
import org.bf.spotservice.collection.application.command.dto.UpdateDto;
import org.bf.spotservice.collection.domain.dto.CollectionIdDto;
import org.bf.spotservice.collection.domain.dto.SpotIdDto;
import org.bf.spotservice.collection.infrastructure.persistence.converter.SpotConverter;
import org.bf.spotservice.spot.application.error.SpotErrorCode;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@ToString
@Getter
@Entity
@Table(name = "p_collections")
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
public class Collection extends Auditable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "collection_id")
    private Long id;

    @Column(nullable = false)
    private UUID userId;

    @Convert(converter = SpotConverter.class)
    private List<Long> spotIds = new ArrayList<>();

    @Column(name = "is_open", nullable = false)
    private Boolean open;

    @Column(name = "collection_name")
    private String name;

    private int fork = 0;

    @Version
    private Long version = 0L;

    public Collection(UUID userId, Boolean open, String name) {
        this.userId = userId;
        this.open = open;
        this.name = name;
    }

    public CollectionIdDto toDto() {

        List<SpotIdDto> spotIdDtos = (this.spotIds == null) ? List.of() : this.spotIds.stream().map(SpotIdDto::new).toList();

        return new CollectionIdDto(
                this.id,
                spotIdDtos
        );
    }

    // 컬렉션 공개 여부 및 이름 업데이트
    public void updateOpenAndName(UpdateDto dto) {
        this.open = dto.isOpen();
        this.name = dto.name();
    }

    public Collection(Boolean open, String name) {
        this.open = open;
        this.name = name;
    }

    public void deleteCollection(String username) {
        softDelete(username);
    }

    public void addSpotId(Long spotId) {

        this.spotIds.add(spotId);
    }

    public void removeSpotId(Long spotId) {

        if (!this.spotIds.contains(spotId)) {
            throw new CustomException(SpotErrorCode.SPOT_NOT_FOUND);
        }
        this.spotIds.remove(spotId);
    }

    public void incrementFork() {
        this.fork += 1;
    }

    public static Collection createForkedCollection(Collection originalCollection, UUID newUserId) {
        Collection forkedCollection = new Collection(newUserId, true, "Forked: " + originalCollection.getName());

        List<Long> originalSpotIds = originalCollection.getSpotIds() == null ? new ArrayList<>() : originalCollection.getSpotIds();

        for (Long spotId : originalSpotIds) {
            forkedCollection.addSpotId(spotId);
        }

        return forkedCollection;
    }
}
