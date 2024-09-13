package com.bigbro.wwooss.v1.entity.quiz;

import com.bigbro.wwooss.v1.entity.base.BaseEntity;
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
public class Quiz extends BaseEntity  {

    @Id
    @Column(name = "quiz_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long quizId;

    @Comment("quiz open 상태")
    @Column(name = "open")
    private boolean open;

    @Comment("퀴즈 정답")
    @Column(name = "answer")
    private String answer;

    @Comment("퀴즈 질문")
    @Column(name = "question")
    private String question;

    @Comment("quiz 이미지")
    @Column(name = "quiz_image_path")
    private String quizImagePath;

    @Comment("토큰 값")
    @Column(name = "token_value")
    private Long tokenValue;

    @Comment("퀴즈 타입")
    @Column(name = "type")
    @Enumerated(EnumType.STRING)
    private QuizType type;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "quiz")
    @Builder.Default
    private List<QuizOption> quizOptionList = new ArrayList<>();

    public void updateOpenStatus(boolean open) {
        this.open = open;
    }

    public static Quiz from(String answer, String question, String quizImagePath,
                            Long tokenValue, QuizType quizType, Boolean open) {
        return Quiz.builder()
                .open(open)
                .answer(answer)
                .question(question)
                .quizImagePath(quizImagePath)
                .tokenValue(tokenValue)
                .type(quizType)
                .build();
    }
}
