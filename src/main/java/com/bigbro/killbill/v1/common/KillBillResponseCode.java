package com.bigbro.killbill.v1.common;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public enum KillBillResponseCode {
    SUCCESS("2000", "안내", "정상 처리되었습니다."),

    DUPLICATION_VALUE("5001", "안내", "중복된 값 입니다."),
    NOT_FOUND_DATA("5002", "안내", "존재하지 않는 값입니다.");

    private final String code;
    private final String title;
    private final String message;
}
