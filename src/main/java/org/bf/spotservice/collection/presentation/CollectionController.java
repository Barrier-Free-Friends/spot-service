package org.bf.spotservice.collection.presentation;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.bf.global.infrastructure.CustomResponse;
import org.bf.global.infrastructure.success.GeneralSuccessCode;
import org.bf.global.security.SecurityUtils;
import org.bf.spotservice.collection.application.command.CollectionCommandService;
import org.bf.spotservice.collection.application.command.dto.CreateDto;
import org.bf.spotservice.collection.application.command.dto.UpdateDto;
import org.bf.spotservice.collection.application.dto.CollectionRankDto;
import org.bf.spotservice.collection.application.query.CollectionQueryService;
import org.bf.spotservice.collection.domain.dto.CollectionDto;
import org.bf.spotservice.collection.domain.dto.CollectionIdDto;
import org.bf.spotservice.collection.presentation.dto.PageResponseDto;
import org.bf.spotservice.collection.presentation.success.CollectionSuccessCode;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@Tag(name = "컬렉션 컨트롤러", description = "Collection 관련 API")
public class CollectionController {

    private final CollectionCommandService collectionCommandService;
    private final CollectionQueryService collectionQueryService;


    @PostMapping("/collection")
    @Operation(description = "유저가 새로운 컬렉션을 만듭니다.")
    public CustomResponse<?> createCollection(CreateDto dto) {

        CollectionIdDto newCollection = collectionCommandService.createCollection(dto);
        return CustomResponse.onSuccess(CollectionSuccessCode.COLLECTION_CREATED, newCollection);
    }

    @DeleteMapping("/collection/{collectionId}")
    @Operation(description = "유저가 자신의 컬렉션을 삭제합니다.")
    public CustomResponse<?> deleteCollection(@PathVariable("collectionId") Long collectionId) {

        collectionCommandService.deleteCollection(collectionId);
        return CustomResponse.onSuccess(CollectionSuccessCode.COLLECTION_DELETED);
    }

    @PatchMapping("/collection/{collectionId}/open")
    @Operation(description = "유저가 자신의 컬렉션 이름과 공개여부를 수정합니다.")
    public CustomResponse<?> updateCollection(@PathVariable("collectionId") Long collectionId, @RequestBody UpdateDto req) {

        collectionCommandService.updateOpenCollection(collectionId, req);
        return CustomResponse.onSuccess(CollectionSuccessCode.COLLECTION_UPDATED);

    }

    @GetMapping("/collection/all")
    @Operation(description = "컬렉션 조회 - 사용자가 갖고 있는 컬렉션을 조회하고 각 컬렉션에는 spot id 목록만 반환합니다.")
    public CustomResponse<?> getCollection() {

        List<CollectionIdDto> collections = collectionQueryService.getCollections();
        return CustomResponse.onSuccess(GeneralSuccessCode.OK, collections);
    }

    @GetMapping("/collection/{collectionId}")
    @Operation(description = "컬렉션 상세 조회 - 특정 컬렉션의 상세 정보를 조회합니다.")
    public CustomResponse<?> getCollection(@PathVariable("collectionId") Long collectionId, SecurityUtils securityUtils) {

        CollectionDto collectionDto = collectionQueryService.getCollection(collectionId, securityUtils.getCurrentUserId());
        return CustomResponse.onSuccess(GeneralSuccessCode.OK, collectionDto);
    }

    @PostMapping("/collection/{spotId}/add/{collectionId}")
    @Operation(description = "컬렉션에 spot 추가")
    public CustomResponse<?> addSpotToCollection(
            @PathVariable("spotId") Long spotId,
            @PathVariable("collectionId") Long collectionId) {

        CollectionIdDto updatedCollection = collectionCommandService.addSpotIdToCollection(collectionId, spotId);
        return CustomResponse.onSuccess(CollectionSuccessCode.COLLECTION_SPOT_ADDED, updatedCollection);
    }

    @DeleteMapping("/collection/{spotId}/remove/{collectionId}")
    @Operation(description = "컬렉션에서 spot 삭제")
    public CustomResponse<?> removeSpotFromCollection(
            @PathVariable("spotId") Long spotId,
            @PathVariable("collectionId") Long collectionId) {

        CollectionIdDto collectionIdDto = collectionCommandService.removeSpotIdFromCollection(collectionId, spotId);
        return CustomResponse.onSuccess(CollectionSuccessCode.COLLECTION_SPOT_REMOVED, collectionIdDto);
    }

    @PostMapping("/collection/{originalCollectionId}/fork")
    @Operation(description = "컬렉션 포크 - 기존 컬렉션을 포크")
    public CustomResponse<?> forkCollection(@PathVariable("originalCollectionId") Long originalCollectionId, @RequestParam("userId") UUID userId) {

        CollectionIdDto forkedCollection = collectionCommandService.forkCollection(originalCollectionId, userId);
        return CustomResponse.onSuccess(CollectionSuccessCode.COLLECTION_FORKED, forkedCollection);
    }

    @GetMapping("/collection/rank")
    @Operation(description = "컬렉션 랭킹 조회 - 포크 수 기준 상위 10개 컬렉션 조회")
    public CustomResponse<?> getCollectionRank(Pageable pageable) {

        Page<CollectionRankDto> rankCollections = collectionQueryService.getCollectionsByFork(pageable);
        return CustomResponse.onSuccess(GeneralSuccessCode.OK, new PageResponseDto<>(rankCollections));
    }
}
