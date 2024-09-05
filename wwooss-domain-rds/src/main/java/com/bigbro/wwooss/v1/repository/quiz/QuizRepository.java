package com.bigbro.wwooss.v1.repository.quiz;

import com.bigbro.wwooss.v1.entity.quiz.Quiz;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QuizRepository extends JpaRepository<Quiz, Long> {
    Quiz findOneByOpenFalseOrderByCreatedAtAsc();
    Quiz findOneByOpenTrueOrderByCreatedAtDesc();
}
