package com.bigbro.wwooss.v1.service.quiz;

import com.bigbro.wwooss.v1.dto.response.quiz.QuizResponse;

public interface QuizService {

    QuizResponse findQuiz(Long quizId);
}
