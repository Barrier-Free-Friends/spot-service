package org.bf.spotservice.collection.application.error;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bf.global.infrastructure.error.BaseErrorCode;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum CollectionErrorCode implements BaseErrorCode {

    COLLECTION_EMPTY(HttpStatus.NO_CONTENT, "204", "등록된 컬렉션이 없습니다."),
    COLLECTION_NOT_FOUND(HttpStatus.NOT_FOUND, "404", "컬렉션을 찾을 수 없습니다."),
    COLLECTION_NOT_PUBLIC(HttpStatus.FORBIDDEN, "403", "비공개 컬렉션입니다.")
    ;

    private final HttpStatus status;
    private final String code;
    private final String message;
    }
