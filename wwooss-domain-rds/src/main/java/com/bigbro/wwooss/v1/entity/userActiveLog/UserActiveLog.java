package com.bigbro.wwooss.v1.entity.userActiveLog;

import com.bigbro.wwooss.v1.entity.base.BaseEntity;
import com.bigbro.wwooss.v1.entity.user.User;
import com.bigbro.wwooss.v1.enumType.ActiveLogType;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.Comment;

import javax.persistence.*;

@Entity
@Table(name = "user_active_log")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@SuperBuilder
public class UserActiveLog extends BaseEntity {

    @Id
    @Column(name = "user_active_log_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userActiveLogId;

    @Comment("인증 - 기록에 대한 인증 여부")
    @Column(name = "verified")
    @Builder.Default
    private Boolean verified = false;

    @Comment("활동 내용")
    @Column(name = "contents")
    private String contents;

    @Comment("record value")
    @Column(name = "value")
    private Double value;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    public void updateVerified(boolean verified) {
        this.verified = verified;
    }

    public static UserActiveLog of(ActiveLogType activeLogType, User user) {
        return UserActiveLog.builder()
                .contents(activeLogType.getContents())
                .value(activeLogType.getValue())
                .user(user)
                .build();
    }
}
