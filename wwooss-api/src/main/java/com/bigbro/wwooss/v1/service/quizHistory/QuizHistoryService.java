package com.bigbro.wwooss.v1.service.quizHistory;

import com.bigbro.wwooss.v1.dto.response.quizHistory.QuizHistoryListResponse;
import org.springframework.data.domain.Pageable;

public interface QuizHistoryService {

    /**
     * quiz history 목록 조회
     * @param userId
     * @param pageable
     * @return
     */
    QuizHistoryListResponse getQuizHistoryList(Long userId, Pageable pageable);
}
