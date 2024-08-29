package com.bigbro.wwooss.v1.service.quizOptions;

import com.bigbro.wwooss.v1.dto.response.notion.QuizDBResultProperty;
import com.bigbro.wwooss.v1.entity.quiz.Quiz;

public interface QuizOptionsService {

    void saveOptions(Quiz quiz, QuizDBResultProperty property);
}
