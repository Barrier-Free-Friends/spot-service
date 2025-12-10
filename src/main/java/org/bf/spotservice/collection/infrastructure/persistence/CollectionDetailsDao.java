package org.bf.spotservice.collection.infrastructure.persistence;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.bf.spotservice.collection.application.dto.CollectionRankDto;
import org.bf.spotservice.collection.domain.Collection;
import org.bf.spotservice.collection.domain.CollectionDetailRepository;
import org.bf.spotservice.collection.domain.CollectionRepository;
import org.bf.spotservice.collection.domain.QCollection;
import org.bf.spotservice.collection.domain.dto.CollectionDto;
import org.bf.spotservice.collection.domain.dto.CollectionIdDto;
import org.bf.spotservice.spot.domain.QSpot;
import org.bf.spotservice.spot.domain.SpotRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class CollectionDetailsDao implements CollectionDetailRepository {

    private final JPAQueryFactory queryFactory;
    private final CollectionRepository collectionRepository;
    private final SpotRepository spotRepository;
    @Override
    public List<CollectionIdDto> findAll() {

        QCollection collection = QCollection.collection;
        QSpot spot = QSpot.spot;

        // Spot 아이디 갯수만큼 Collection 조회 -> Collection 1개 -> List
        List<CollectionIdDto> items = queryFactory
                .select(Projections.constructor(CollectionIdDto.class, collection.id, spot))
                .leftJoin(spot).on(spot.id.in(collection.spotIds))
                .fetch();

        return items;
    }

    @Override
    public Page<CollectionRankDto> findCollectionByForkDesc(Pageable pageable) {

        QCollection collection = QCollection.collection;

        List<Collection> contents = queryFactory
                .selectFrom(collection)
                .where(
                        collection.open.isTrue(),
                        collection.deletedAt.isNull() // soft delete 된 데이터 패스
                )
                .orderBy(collection.fork.desc(), collection.createdAt.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        // 카운트 쿼리 따로 작성
        JPAQuery<Long> countQuery = queryFactory
                .select(collection.count())
                .from(collection)
                .where(collection.open.isTrue(), collection.deletedAt.isNull());

        // entity -> dto 변환 -> @Converter로 적용된 spotIds 가져온다
        List<CollectionRankDto> rankDtos = contents.stream().map(CollectionRankDto::from).toList();

        // count 쿼리 처리를 위한 PageableExecutionUtils 사용
        return PageableExecutionUtils.getPage(rankDtos, pageable, countQuery::fetchOne);

    }

}
