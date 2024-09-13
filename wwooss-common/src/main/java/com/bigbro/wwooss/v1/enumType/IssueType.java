package com.bigbro.wwooss.v1.enumType;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public enum IssueType {
    QUIZ(5L),
    LOGIN(3L),
    INVITE(50L),
    ALARM(10L),
    CLEAN_TRASH(2L),
    CHECK_REPORT(10L),
    RECORD_RECYCLE(1L),
    AUTH_RECYCLE(10L),
    COMPLIMENT_CARD(5L),
    SIGN_UP(100L);

    private final Long value;
}
