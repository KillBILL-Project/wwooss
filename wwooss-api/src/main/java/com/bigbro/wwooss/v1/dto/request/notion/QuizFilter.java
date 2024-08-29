package com.bigbro.wwooss.v1.dto.request.notion;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class QuizFilter {
    private String property;
    private QuizStatus status;

    public static QuizFilter of(String property, QuizStatus status) {
        return QuizFilter.builder()
                .property(property)
                .status(status)
                .build();
    }
}
