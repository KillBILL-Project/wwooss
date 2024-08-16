package com.bigbro.wwooss.v1.enumType;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public enum ActiveLogType {
    QUIZ("퀴즈 활동", 0.1D);

    private final String contents;
    private final Double value;
}
