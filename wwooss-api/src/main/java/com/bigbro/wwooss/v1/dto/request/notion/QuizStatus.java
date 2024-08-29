package com.bigbro.wwooss.v1.dto.request.notion;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class QuizStatus {
    private String equals;

    public static QuizStatus from(String equals) {
        return QuizStatus.builder()
                .equals(equals)
                .build();
    }
}
