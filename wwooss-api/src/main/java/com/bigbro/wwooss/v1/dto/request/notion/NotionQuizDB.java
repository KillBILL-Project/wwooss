package com.bigbro.wwooss.v1.dto.request.notion;

import com.bigbro.wwooss.v1.dto.response.notion.QuizDBResultProperty;
import com.bigbro.wwooss.v1.enumType.IssueType;
import com.bigbro.wwooss.v1.enumType.QuizType;
import lombok.Builder;

@Builder
public class NotionQuizDB {
    private String answer;
    private String question;
    private String quizImagePath;
    private Long tokenValue;
    private QuizType quizType;


    public static NotionQuizDB of(QuizDBResultProperty property) {
        return NotionQuizDB.builder()
                .answer(property.getAnswer().getRich_text().get(0).getPlain_text())
                .question(property.getQuestion().getRich_text().get(0).getPlain_text())
                .tokenValue(IssueType.QUIZ.getValue())
                .quizType(QuizType.valueOf(property.getType().getSelect().getName()))
                .build();
    }
}
