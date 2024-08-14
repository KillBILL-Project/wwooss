package com.bigbro.wwooss.v1.entity.quiz;

import com.bigbro.wwooss.v1.entity.token.Token;
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

    @Comment("퀴즈 정답")
    @Column(name = "answer")
    private String answer;

    @Comment("퀴즈 질문")
    @Column(name = "question")
    private String question;

    @Comment("quiz 이미지")
    @Column(name = "quiz_image_path")
    private String quizImagePath;

    @Comment("퀴즈 타입")
    @Column(name = "type")
    @Enumerated(EnumType.STRING)
    private QuizType type;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "token_id")
    private Token token;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "quiz")
    @Builder.Default
    private List<QuizHistory> quizHistoryList = new ArrayList<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "quiz")
    @Builder.Default
    private List<QuizOption> quizOptionList = new ArrayList<>();
}
