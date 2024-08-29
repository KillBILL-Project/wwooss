package com.bigbro.wwooss.v1.dto.request.notion;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class QuizDBRequest {
    private Integer page_size;
    private QuizFilter filter;

    public static QuizDBRequest of(Integer pageSize, QuizFilter filter) {
        return QuizDBRequest.builder()
                .page_size(pageSize)
                .filter(filter)
                .build();
    }

}
