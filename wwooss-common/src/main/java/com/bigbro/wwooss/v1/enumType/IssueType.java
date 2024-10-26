package com.bigbro.wwooss.v1.enumType;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public enum IssueType {
    QUIZ(5L, 5L),
    LOGIN(3L, 3L),
    INVITE(50L, 1000L),
    ALARM(10L, 10L),
    CLEAN_TRASH(2L, 10L),
    CHECK_REPORT(10L, 10L),
    RECORD_RECYCLE(1L, 100L),
    AUTH_RECYCLE(10L, 1000L),
    COMPLIMENT_CARD(5L, 999999L),
    SIGN_UP(100L, 100L);

    private final Long value;
    private final Long maxValue;
}
