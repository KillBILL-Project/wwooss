package com.bigbro.wwooss.v1.entity.quiz;

import com.bigbro.wwooss.v1.enumType.QuizType;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.Comment;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "quiz")
@SuperBuilder
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Quiz {

    @Id
    @Column(name = "quiz_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long quizId;

    @Comment("quiz open 상태")
    @Column(name = "open")
    private boolean open;

    @Comment("정답률")
    @Column(name = "correct_rate")
    private Double correctRate;

    @Comment("퀴즈 정답")
    @Column(name = "answer")
    private String answer;

    @Comment("퀴즈 타입")
    @Column(name = "type")
    @Enumerated(EnumType.STRING)
    private QuizType type;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "quiz")
    @Builder.Default
    private List<QuizHistory> quizHistoryList = new ArrayList<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "quiz")
    @Builder.Default
    private List<QuizOption> quizOptionList = new ArrayList<>();
}
