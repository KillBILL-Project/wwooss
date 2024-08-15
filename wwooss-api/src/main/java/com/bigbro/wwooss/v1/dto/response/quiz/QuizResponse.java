package com.bigbro.wwooss.v1.dto.response.quiz;

import com.bigbro.wwooss.v1.dto.option.OptionResponse;
import com.bigbro.wwooss.v1.entity.quiz.Quiz;
import com.bigbro.wwooss.v1.enumType.QuizType;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class QuizResponse {
    private Long quizId;
    private String question;
    private Double correctRate;
    private QuizType quizType;
    private String imagePath;
    private List<OptionResponse> options;

    public static QuizResponse of(Quiz quiz, Double correctRate, String imagePath, List<OptionResponse> options) {
        return QuizResponse.builder()
                .quizId(quiz.getQuizId())
                .question(quiz.getQuestion())
                .quizType(quiz.getType())
                .correctRate(correctRate)
                .imagePath(imagePath)
                .options(options)
                .build();
    }
}
