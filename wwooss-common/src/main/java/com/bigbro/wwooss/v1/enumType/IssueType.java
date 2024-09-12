package com.bigbro.wwooss.v1.enumType;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public enum IssueType {
    QUIZ(5D),
    LOGIN(3D),
    INVITE(50D),
    ALARM(10D),
    CLEAN_TRASH(2D),
    CHECK_REPORT(10D),
    RECORD_RECYCLE(1D),
    AUTH_RECYCLE(10D),
    COMPLIMENT_CARD(5D),
    SIGN_UP(100D);

    private final Double value;
}
