package com.bigbro.wwooss.v1.entity.user;

import com.bigbro.wwooss.v1.entity.base.BaseEntity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;

@Entity
@Table(name = "login_log")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@SuperBuilder
public class LoginLog extends BaseEntity {
    @Id
    @Column(name = "login_log_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long loginLogId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "continuity")
    private Long continuity;

    public static LoginLog of(User user, Long continuity) {
        return LoginLog.builder()
                .user(user)
                .continuity(continuity)
                .build();
    }
}
