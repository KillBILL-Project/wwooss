package com.bigbro.wwooss.v1.enumType;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public enum QuizType {
    MULTI_CHOICE("객관식"),
    OX("OX 퀴즈"),
    SHORT_ANSWER("단답형 주관식");

    private final String description;
}
