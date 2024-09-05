package com.bigbro.wwooss.v1.service.quizHistory;

import com.bigbro.wwooss.v1.dto.response.quizHistory.QuizHistoryListResponse;
import com.bigbro.wwooss.v1.entity.quiz.Quiz;
import com.bigbro.wwooss.v1.entity.user.User;
import org.springframework.data.domain.Pageable;

public interface QuizHistoryService {

    /**
     * quiz history 목록 조회
     * @param userId
     * @param pageable
     * @return
     */
    QuizHistoryListResponse getQuizHistoryList(Long userId, Pageable pageable);

    /**
     * 배치에 의한 모든 유저 퀴즈 생성
     * @param quiz
     */
    void createQuizHistoryAllUser(Quiz quiz);

    /**
     *  quiz 발급된 상태로 변경
     * @param quizHistoryId
     */
    void updateStatusToIssued(Long quizHistoryId);

    /**
     * 신규 가입자 최신 퀴즈 넣어주기
     */
    void addQuizToNewUser(Quiz quiz, User user);
}
