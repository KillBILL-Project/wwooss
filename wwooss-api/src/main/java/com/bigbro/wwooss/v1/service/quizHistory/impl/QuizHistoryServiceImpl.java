package com.bigbro.wwooss.v1.service.quizHistory.impl;

import com.bigbro.wwooss.v1.dto.response.quizHistory.QuizHistoryInfo;
import com.bigbro.wwooss.v1.dto.response.quizHistory.QuizHistoryListResponse;
import com.bigbro.wwooss.v1.entity.quiz.QuizHistory;
import com.bigbro.wwooss.v1.entity.user.User;
import com.bigbro.wwooss.v1.exception.DataNotFoundException;
import com.bigbro.wwooss.v1.repository.quizHistory.QuizHistoryRepository;
import com.bigbro.wwooss.v1.repository.user.UserRepository;
import com.bigbro.wwooss.v1.response.WwoossResponseCode;
import com.bigbro.wwooss.v1.service.quizHistory.QuizHistoryService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class QuizHistoryServiceImpl implements QuizHistoryService {

    private final QuizHistoryRepository quizHistoryRepository;

    private final UserRepository userRepository;

    @Override
    @Transactional(readOnly = true)
    public QuizHistoryListResponse getQuizHistoryList(Long userId, Pageable pageable) {
        User user = userRepository.findById(userId).orElseThrow(() -> new DataNotFoundException(WwoossResponseCode.NOT_FOUND_DATA, "존재하지 않는 유저입니다."));
        Slice<QuizHistory> quizHistoryList = quizHistoryRepository.findQuizHistoryByUserOrderByCreatedAtDesc(user, pageable);

        List<QuizHistoryInfo> quizHistoryInfoList = quizHistoryList.getContent().stream().map(QuizHistoryInfo::of).toList();
        return QuizHistoryListResponse.of(quizHistoryList.hasNext(), quizHistoryInfoList);
    }
}
