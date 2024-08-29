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
public class QuizDBPropertyTextType {
    private String id;
    private List<QuizDBRichText> rich_text;
}
