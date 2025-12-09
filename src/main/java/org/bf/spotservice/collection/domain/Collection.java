package org.bf.spotservice.collection.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.ToString;
import org.bf.spotservice.collection.domain.dto.CollectionIdDto;
import org.bf.spotservice.collection.domain.dto.SpotIdDto;
import org.bf.spotservice.collection.infrastructure.persistence.converter.SpotConverter;

import java.util.List;
import java.util.UUID;

@ToString
@Getter
@Entity
@Table(name = "p_collections")
public class Collection {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "collection_id")
    private Long id;

    @Column(nullable = false, unique = true)
    private UUID userId;

    @Convert(converter = SpotConverter.class)
    private List<Long> spotIds;

    @Column(name = "is_open", nullable = false)
    private Boolean open;

    @Column(name = "collection_name")
    private String name;

    public CollectionIdDto toDto() {
        return new CollectionIdDto(
                this.id,
                this.spotIds.stream().map(SpotIdDto::new).toList()
        );
    }

    // 공개 여부 업데이트
    public void updateOpen(Boolean open, String name) {
        this.open = open;
        this.name = name;
    }


}
