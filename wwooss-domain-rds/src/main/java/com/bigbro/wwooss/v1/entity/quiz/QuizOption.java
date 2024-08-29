package com.bigbro.wwooss.v1.entity.quiz;

import com.bigbro.wwooss.v1.entity.base.BaseEntity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.Comment;

import javax.persistence.*;

@Entity
@Table(name = "quiz_option")
@SuperBuilder
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class QuizOption extends BaseEntity  {

    @Id
    @Column(name = "option_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long optionId;

    @Comment("문제 선택지 값")
    @Column(name = "value")
    private String value;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "quiz_id")
    private Quiz quiz;

    public static QuizOption of(Quiz quiz, String value) {
        return QuizOption.builder()
                .quiz(quiz)
                .value(value)
                .build();
    }
}
