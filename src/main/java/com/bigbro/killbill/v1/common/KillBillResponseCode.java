package com.bigbro.killbill.v1.common;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public enum KillBillResponseCode {
    SUCCESS("2000", "안내", "정상 처리 되었습니다."),

    // bad request => 4XXX
    NOT_FOUND_DATA("4000", "안내", "존재 하지 않는 값 입니다."),

    // server error => 5XXX
    DUPLICATION_VALUE("5000", "안내", "중복된 값 입니다.");

    private final String code;
    private final String title;
    private final String message;
}
