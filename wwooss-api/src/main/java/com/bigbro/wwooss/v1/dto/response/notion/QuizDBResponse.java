package com.bigbro.wwooss.v1.dto.response.notion;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class QuizDBResponse {
    private String object;
    private List<QuizDBResult> results;
    private String next_cursor;
    private Boolean has_more;
}
