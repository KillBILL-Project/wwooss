package com.bigbro.wwooss.v1.enumType;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public enum DayOfWeek {
    MON(1),
    TUES(2),
    WED(3),
    THURS(4),
    FRI(5),
    SAT(6),
    SUN(7);

    private final int value;
}
