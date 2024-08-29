package com.bigbro.wwooss.v1.dto.request.notion;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class QuizDBCursorRequest {
    private Integer page_size;
    private QuizFilter filter;
    private String start_cursor;

    public static QuizDBCursorRequest of(Integer pageSize, QuizFilter filter, String startCursor) {
        return QuizDBCursorRequest.builder()
                .page_size(pageSize)
                .filter(filter)
                .start_cursor(startCursor)
                .build();
    }
}
