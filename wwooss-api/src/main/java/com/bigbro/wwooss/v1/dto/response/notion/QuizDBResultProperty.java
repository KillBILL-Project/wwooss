package com.bigbro.wwooss.v1.dto.response.notion;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class QuizDBResultProperty {
    private QuizDBPropertyType type;
    private QuizDBPropertyTextType question;
    private QuizDBPropertyStatus status;
    private QuizDBPropertyOptions options;
    private QuizDBPropertyTextType image;
    private QuizDBPropertyAnswer answer;
    private QuizDBPropertyTextType tokenValue;
}
