package com.bigbro.killbill.v1.common;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public enum KillBillResponseCode {
    SUCCESS("2000", "안내", "정상 처리되었습니다.");

    private final String code;
    private final String title;
    private final String message;
}
