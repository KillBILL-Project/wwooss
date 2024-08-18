package com.bigbro.wwooss.v1.entity;

import lombok.Builder;
import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

@Getter
@Builder
@RedisHash(value = "quiz_correct_rate")
public class QuizCorrectRate {

    @Id
    private Long quizId;
    private Long totalCount;
    private Long correctCount;
    private Long correctRate;

    public void updateCorrectRate(Long totalCount, Long correctCount, Long correctRate) {
        this.totalCount = totalCount;
        this.correctCount = correctCount;
        this.correctRate = correctRate;
    }

    public static QuizCorrectRate of(Long totalCount, Long correctCount, Long correctRate) {
        return QuizCorrectRate.builder()
                .totalCount(totalCount)
                .correctCount(correctCount)
                .correctRate(correctRate)
                .build();
    }
}
