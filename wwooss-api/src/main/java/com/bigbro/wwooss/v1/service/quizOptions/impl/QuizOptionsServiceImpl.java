package com.bigbro.wwooss.v1.service.quizOptions.impl;

import com.bigbro.wwooss.v1.dto.response.notion.QuizDBMultiSelect;
import com.bigbro.wwooss.v1.dto.response.notion.QuizDBResultProperty;
import com.bigbro.wwooss.v1.entity.quiz.Quiz;
import com.bigbro.wwooss.v1.entity.quiz.QuizOption;
import com.bigbro.wwooss.v1.repository.quizOptions.QuizOptionsRepository;
import com.bigbro.wwooss.v1.service.quizOptions.QuizOptionsService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class QuizOptionsServiceImpl implements QuizOptionsService {

    private final QuizOptionsRepository quizOptionsRepository;

    @Override
    @Transactional
    public void saveOptions(Quiz quiz, QuizDBResultProperty property) {
        List<QuizDBMultiSelect> multiSelect = property.getOptions().getMulti_select();
        List<String> selectOptions = multiSelect.stream().map(QuizDBMultiSelect::getName).toList();

        List<QuizOption> optionList = new ArrayList<>();
        selectOptions.forEach((optionValue) -> optionList.add(QuizOption.of(quiz, optionValue)));

        quizOptionsRepository.saveAll(optionList);
    }
}

