package com.bigbro.wwooss.v1.repository;

import com.bigbro.wwooss.v1.entity.QuizCorrectRate;
import org.springframework.data.repository.CrudRepository;

public interface RedisRepository extends CrudRepository<QuizCorrectRate, Long> {
    QuizCorrectRate findQuizCorrectRateByQuizId(Long quizId);
}
