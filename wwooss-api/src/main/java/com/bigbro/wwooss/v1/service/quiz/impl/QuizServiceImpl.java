package com.bigbro.wwooss.v1.service.quiz.impl;

import com.bigbro.wwooss.v1.dto.option.OptionResponse;
import com.bigbro.wwooss.v1.dto.response.quiz.QuizResponse;
import com.bigbro.wwooss.v1.entity.quiz.Quiz;
import com.bigbro.wwooss.v1.entity.quiz.QuizOption;
import com.bigbro.wwooss.v1.exception.DataNotFoundException;
import com.bigbro.wwooss.v1.repository.quiz.QuizRepository;
import com.bigbro.wwooss.v1.response.WwoossResponseCode;
import com.bigbro.wwooss.v1.service.quiz.QuizService;
import com.bigbro.wwooss.v1.utils.ImageUtil;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class QuizServiceImpl implements QuizService {

    private final QuizRepository quizRepository;

    private final ImageUtil imageUtil;

    @Override
    @Transactional(readOnly = true)
    public QuizResponse findQuiz(Long quizId) {
        Quiz quiz = quizRepository.findById(quizId).orElseThrow(() -> new DataNotFoundException(WwoossResponseCode.NOT_FOUND_DATA, "퀴즈를 찾을 수 없습니다."));
        List<QuizOption> quizOptionList = quiz.getQuizOptionList();
        List<OptionResponse> options = quizOptionList.stream().map(OptionResponse::from).toList();

        return QuizResponse.of(quiz, 1.0D, imageUtil.baseQuizImagePath(), options);
    }
}
