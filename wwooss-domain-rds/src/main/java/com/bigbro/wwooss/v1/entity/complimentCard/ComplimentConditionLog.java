package com.bigbro.wwooss.v1.entity.complimentCard;

import com.bigbro.wwooss.v1.entity.base.BaseEntity;
import com.bigbro.wwooss.v1.entity.user.User;
import com.bigbro.wwooss.v1.enumType.ComplimentType;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.Comment;

import javax.persistence.*;

@Entity
@Table(name = "compliment_condition_log")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@SuperBuilder
public class ComplimentConditionLog extends BaseEntity {

    @Id
    @Column(name = "compliment_condition_log_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long complimentConditionLogId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Comment("연속 몇번째")
    @Column(name = "continuity")
    private Long continuity;

    @Comment("칭찬 카드 타입")
    @Column(name = "compliment_type")
    @Enumerated(value = EnumType.STRING)
    private ComplimentType complimentType;

    public static ComplimentConditionLog of(User user, long continuity, ComplimentType complimentType) {
        return ComplimentConditionLog.builder()
                .user(user)
                .continuity(continuity)
                .complimentType(complimentType)
                .build();
    }

}
