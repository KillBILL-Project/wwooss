package com.bigbro.wwooss.v1.dto.response.notion;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class QuizDBResult {
    private String id;
    private QuizDBResultProperty properties;
}
