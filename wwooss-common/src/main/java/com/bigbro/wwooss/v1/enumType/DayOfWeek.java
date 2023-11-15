package com.bigbro.wwooss.v1.enumType;

import static com.bigbro.wwooss.v1.response.WwoossResponseCode.INCORRECT_DATA;

import com.bigbro.wwooss.v1.exception.IncorrectDataException;
import java.util.Arrays;
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

    public static DayOfWeek from(int value) {
        return Arrays.stream(DayOfWeek.values())
                .filter((day) -> day.getValue() == value)
                .findFirst()
                .orElseThrow(() -> new IncorrectDataException(INCORRECT_DATA, "해당되는 요일은 존재하지 않습니다."));
    }
}
