package com.bigbro.wwooss.v1.entity.quiz;

import com.bigbro.wwooss.v1.entity.base.BaseEntity;
import com.bigbro.wwooss.v1.entity.user.User;
import com.bigbro.wwooss.v1.enumType.QuizStatusType;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.Comment;

import javax.persistence.*;

@Entity
@Table(name = "quiz_history")
@SuperBuilder
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class QuizHistory extends BaseEntity {

    @Id
    @Column(name = "quiz_history_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long quizHistoryId;

    @Comment("퀴즈 풀이 상태")
    @Column(name = "status")
    @Enumerated(value = EnumType.STRING)
    private QuizStatusType status;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "quiz_id")
    private Quiz quiz;

    public void updateQuizStatus(QuizStatusType status) {
        this.status = status;
    }
}
