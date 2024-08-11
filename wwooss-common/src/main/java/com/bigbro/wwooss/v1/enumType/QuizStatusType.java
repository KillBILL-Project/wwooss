package com.bigbro.wwooss.v1.enumType;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public enum QuizStatusType {
    UNSOLVED("문제 풀지 않음"),
    PASS("풀이 정답"),
    FAIL("풀이 실패"),
    ISSUED("토큰 발급");

    private final String description;
}
