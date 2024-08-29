package com.bigbro.wwooss.v1.dto.response.notion;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class QuizDBPropertyStatus {
    private String id;
    private QuizDBName name;
}
