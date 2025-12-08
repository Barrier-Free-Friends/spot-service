package org.bf.spotservice.spot.infrastructure.persisteence;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.bf.spotservice.collection.domain.dto.SpotDto;
import org.bf.spotservice.spot.domain.QSpot;
import org.bf.spotservice.spot.domain.SpotDetailRepository;
import org.springframework.stereotype.Repository;

@Repository
public class SpotDetailsDao implements SpotDetailRepository {

    private final JPAQueryFactory queryFactory;

    public SpotDetailsDao(JPAQueryFactory queryFactory) {
        this.queryFactory = queryFactory;
    }

    @Override
    public SpotDto findByLatitudeAndLongitude(double latitude, double longitude) {

         QSpot spot = QSpot.spot;

        SpotDto spotDto = queryFactory
                .select(Projections.constructor(SpotDto.class, spot.id, spot.name, spot.address, spot.latitude, spot.longitude))
                .from(spot)
                .where(spot.latitude.eq(latitude)
                        .and(spot.longitude.eq(longitude)))
                .fetchFirst();

        return spotDto;
    }
}
