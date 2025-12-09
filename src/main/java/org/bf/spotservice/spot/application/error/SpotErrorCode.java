package org.bf.spotservice.spot.application.error;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bf.global.infrastructure.error.BaseErrorCode;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum SpotErrorCode implements BaseErrorCode {

    SPOT_NOT_FOUND(HttpStatus.NOT_FOUND, "404", "장소를 찾을 수 없습니다."),
    ;


    private final HttpStatus status;
    private final String code;
    private final String message;
}
