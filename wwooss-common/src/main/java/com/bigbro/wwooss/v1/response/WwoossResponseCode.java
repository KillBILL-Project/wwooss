package com.bigbro.wwooss.v1.response;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public enum WwoossResponseCode {
    // Success
    SUCCESS(HttpStatus.OK, "안내", "정상 처리 되었습니다."),
    CREATED_SUCCESS(HttpStatus.CREATED, "안내", "정상적으로 생성 되었습니다."),
    NO_CONTENT_SUCCESS(HttpStatus.NO_CONTENT, "안내", "정상 처리 되었습니다."),

    // client error
    UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "안내", "인증이 필요합니다."),
    FORBIDDEN(HttpStatus.FORBIDDEN, "안내", "접근 거부"),
    NOT_FOUND_DATA(HttpStatus.NOT_FOUND, "안내", "존재 하지 않는 값 입니다."),
    INVALID_TOKEN(HttpStatus.BAD_REQUEST, "안내", "토큰 파싱 에러입니다. 토큰을 확인해주세요."),

    // server error
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "안내", "서버 내부 오류입니다."),
    DUPLICATION_VALUE(HttpStatus.INTERNAL_SERVER_ERROR, "안내", "중복된 값 입니다."),
    INCORRECT_DATA(HttpStatus.INTERNAL_SERVER_ERROR, "안내", "잘못된 유형의 데이터입니다."),
    DOCUMENT_BULK_INSERT_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "안내", "ES Document Indexing Error");

    private final HttpStatus code;
    private final String title;
    private final String message;
}
