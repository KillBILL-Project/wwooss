package com.bigbro.wwooss.v1.service;

public interface RedisService {
    /**
     * quiz 정답률 조회
     * @param quizId
     * @return
     */
    Long getQuizCorrectRate(Long quizId);

    void updateQuizCorrectRate(Long quizId, Boolean isPass);

    void createDefaultQuizCorrectRate(Long totalCount, Long correctCount, Long quizId);
}
