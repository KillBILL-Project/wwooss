package com.bigbro.wwooss.v1.dto.option;

import com.bigbro.wwooss.v1.entity.quiz.QuizOption;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class OptionResponse {
    private Long optionId;
    private String value;

    public static OptionResponse from(QuizOption option) {
        return OptionResponse.builder()
                .optionId(option.getOptionId())
                .value(option.getValue())
                .build();
    }
}
