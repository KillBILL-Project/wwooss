package com.bigbro.killbill.v1.common;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public enum KillBillResponseCode {
    SUCCESS("2000", "안내", "정상 처리되었습니다."),

    DUPLICATION_VALUE("5001", "안내", "중복된 값입니다.");

    private final String code;
    private final String title;
    private final String message;
}
