package com.bigbro.wwooss.v1.service.impl;

import com.bigbro.wwooss.v1.entity.QuizCorrectRate;
import com.bigbro.wwooss.v1.repository.RedisRepository;
import com.bigbro.wwooss.v1.service.RedisService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class RedisServiceImpl implements RedisService {

    private final RedisRepository redisRepository;


    @Override
    public Long getQuizCorrectRate(Long quizId) {
        QuizCorrectRate quizCorrectRate = redisRepository.findQuizCorrectRateByQuizId(quizId);
        if(Objects.isNull(quizCorrectRate)) return -1L;

        return quizCorrectRate.getCorrectRate();
    }

    @Override
    public void updateQuizCorrectRate(Long quizId, Boolean isPass) {
        QuizCorrectRate quizCorrectRate = redisRepository.findQuizCorrectRateByQuizId(quizId);
        if(Objects.isNull(quizCorrectRate)) return;

        long totalCnt = quizCorrectRate.getTotalCount() + 1;
        long correctCnt = isPass ? quizCorrectRate.getCorrectCount() + 1 : quizCorrectRate.getCorrectCount();

        quizCorrectRate.updateCorrectRate(totalCnt, correctCnt, (correctCnt * 100) / totalCnt);
    }

    @Override
    public void createDefaultQuizCorrectRate(Long totalCount, Long correctCount) {
        QuizCorrectRate quizCorrectRate = QuizCorrectRate.of(totalCount, correctCount, (correctCount * 100) / totalCount);
        redisRepository.save(quizCorrectRate);
    }
}
