package com.bigbro.wwooss.v1.service.quiz;

import com.bigbro.wwooss.v1.dto.response.notion.QuizDBResultProperty;
import com.bigbro.wwooss.v1.dto.response.quiz.QuizResponse;
import com.bigbro.wwooss.v1.entity.quiz.Quiz;
import com.bigbro.wwooss.v1.enumType.QuizStatusType;

public interface QuizService {

    QuizResponse findQuiz(Long quizId);

    QuizStatusType solveQuiz(Long userId, Long quizId, String answer);

    Quiz saveQuiz(QuizDBResultProperty properties);

    Quiz findNewQuiz();

    Quiz findLatestQuiz();
}
