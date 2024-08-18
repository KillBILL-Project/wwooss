package com.bigbro.wwooss.v1.repository.quizHistory;

import com.bigbro.wwooss.v1.entity.quiz.Quiz;
import com.bigbro.wwooss.v1.entity.quiz.QuizHistory;
import com.bigbro.wwooss.v1.entity.user.User;
import com.bigbro.wwooss.v1.enumType.QuizStatusType;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QuizHistoryRepository extends JpaRepository<QuizHistory, Long> {

    Slice<QuizHistory> findQuizHistoryByUserOrderByCreatedAtDesc(User user, Pageable pageable);
    QuizHistory findQuizHistoryByUserAndQuiz(User user, Quiz quiz);
    Long countQuizHistoryByStatusOrStatus(QuizStatusType quizStatusType, QuizStatusType statusType);
    Long countQuizHistoryByStatus(QuizStatusType quizStatusType);
}
