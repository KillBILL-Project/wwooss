package com.bigbro.wwooss.v1.service.impl;

import com.bigbro.wwooss.v1.entity.QuizCorrectRate;
import com.bigbro.wwooss.v1.repository.RedisRepository;
import com.bigbro.wwooss.v1.service.RedisService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
@Slf4j
public class RedisServiceImpl implements RedisService {

    private final RedisRepository redisRepository;


    @Override
    public Long getQuizCorrectRate(Long quizId) {
        try {
            QuizCorrectRate quizCorrectRate = redisRepository.findQuizCorrectRateByQuizId(quizId);
            if(Objects.isNull(quizCorrectRate)) return -1L;

            return quizCorrectRate.getCorrectRate();
        } catch (Exception e) {
            log.error("get Quiz Correct Rate : {}", e.getMessage(), e);
            return -1L;
        }
    }

    @Override
    public void updateQuizCorrectRate(Long quizId, Boolean isPass) {
        try {
            QuizCorrectRate quizCorrectRate = redisRepository.findQuizCorrectRateByQuizId(quizId);
            if(Objects.isNull(quizCorrectRate)) return;

            long totalCnt = quizCorrectRate.getTotalCount() + 1;
            long correctCnt = isPass ? quizCorrectRate.getCorrectCount() + 1 : quizCorrectRate.getCorrectCount();

            quizCorrectRate.updateCorrectRate(totalCnt, correctCnt, (correctCnt * 100) / totalCnt);
        } catch (Exception e) {
            log.error("update Quiz Correct Rate : {}", e.getMessage(), e);
        }

    }

    @Override
    public void createDefaultQuizCorrectRate(Long totalCount, Long correctCount, Long quizId) {
        try {
            long correctRate = totalCount == 0 ? 0 : ((correctCount * 100) / totalCount);
            QuizCorrectRate quizCorrectRate = QuizCorrectRate.of(totalCount, correctCount, correctRate, quizId);
            redisRepository.save(quizCorrectRate);
        } catch (Exception e) {
            log.error("create Quiz Correct Rate : {}", e.getMessage(), e);
        }
    }
}
