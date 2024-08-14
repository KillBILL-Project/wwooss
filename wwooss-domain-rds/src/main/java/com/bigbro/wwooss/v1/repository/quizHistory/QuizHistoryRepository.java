package com.bigbro.wwooss.v1.repository.quizHistory;

import com.bigbro.wwooss.v1.entity.quiz.QuizHistory;
import com.bigbro.wwooss.v1.entity.user.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QuizHistoryRepository extends JpaRepository<QuizHistory, Long> {

    Slice<QuizHistory> findQuizHistoryByUser(User user, Pageable pageable);
}
