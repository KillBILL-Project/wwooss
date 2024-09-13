package com.bigbro.wwooss.v1.dto.response.quizHistory;

import com.bigbro.wwooss.v1.entity.quiz.QuizHistory;
import com.bigbro.wwooss.v1.enumType.QuizStatusType;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Builder
@Getter
public class QuizHistoryInfo {
    private Long quizHistoryId;
    private Long quizId;
    private QuizStatusType status;
    private Long tokenValue;
    private LocalDateTime createdAt;

    public static QuizHistoryInfo of(QuizHistory quizHistory) {
        return QuizHistoryInfo.builder()
                .quizHistoryId(quizHistory.getQuizHistoryId())
                .quizId(quizHistory.getQuiz().getQuizId())
                .status(quizHistory.getStatus())
                .tokenValue(quizHistory.getQuiz().getTokenValue())
                .createdAt(quizHistory.getCreatedAt())
                .build();
    }
}
