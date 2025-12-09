package org.bf.spotservice.collection.infrastructure.persistence;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.bf.spotservice.collection.domain.CollectionDetailRepository;
import org.bf.spotservice.collection.domain.CollectionRepository;
import org.bf.spotservice.collection.domain.QCollection;
import org.bf.spotservice.collection.domain.dto.CollectionDto;
import org.bf.spotservice.spot.domain.QSpot;
import org.bf.spotservice.spot.domain.SpotRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class CollectionDetailsDao implements CollectionDetailRepository {

    private final JPAQueryFactory queryFactory;
    private final CollectionRepository collectionRepository;
    private final SpotRepository spotRepository;
    @Override
    public List<CollectionDto> findAll() {

        QCollection collection = QCollection.collection;
        QSpot spot = QSpot.spot;

        // Spot 아이디 갯수만큼 Collection 조회 -> Collection 1개 -> List
        List<CollectionDto> items = queryFactory
                .select(Projections.constructor(CollectionDto.class, collection.id, spot))
                .leftJoin(spot).on(spot.id.in(collection.spotIds))
                .fetch();

        return items;
    }

//    public CollectionDto findById(Long id) {
//
//        Collection collection = collectionRepository.findById(id).orElse(null);
//        if (collection != null) {
//            // Spot 목록
//            List<Spot> spots = spotRepository.findAllById(collection.getSpots());
//
//        }
//
//        return new CollectionDto(....);
//    }
}
