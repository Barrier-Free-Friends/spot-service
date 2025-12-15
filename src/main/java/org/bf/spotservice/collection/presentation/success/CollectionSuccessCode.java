package org.bf.spotservice.collection.presentation.success;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bf.global.infrastructure.success.BaseSuccessCode;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum CollectionSuccessCode implements BaseSuccessCode {


    COLLECTION_CREATED(HttpStatus.CREATED, "COLLECTION-001", "컬렉션이 성공적으로 생성되었습니다."),
    COLLECTION_UPDATED(HttpStatus.OK, "COLLECTION-002", "컬렉션이 성공적으로 수정되었습니다."),
    COLLECTION_DELETED(HttpStatus.OK, "COLLECTION-003", "컬렉션이 성공적으로 삭제되었습니다."),
    COLLECTION_SPOT_ADDED(HttpStatus.OK, "COLLECTION-004", "컬렉션에 스팟이 성공적으로 추가되었습니다."),
    COLLECTION_SPOT_REMOVED(HttpStatus.NO_CONTENT, "COLLECTION-005", "컬렉션에서 스팟이 성공적으로 제거되었습니다."),
    COLLECTION_FORKED(HttpStatus.CREATED, "COLLECTION-006", "컬렉션이 성공적으로 포크되었습니다.");

    private final HttpStatus status;
    private final String code;
    private final String message;
}
