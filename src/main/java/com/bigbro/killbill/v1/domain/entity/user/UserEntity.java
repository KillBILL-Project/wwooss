package com.bigbro.killbill.v1.domain.entity.user;

import com.bigbro.killbill.v1.domain.entity.base.BaseEntity;
import com.bigbro.killbill.v1.enumType.LoginType;
import com.bigbro.killbill.v1.enumType.UserRole;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.Comment;

import javax.persistence.*;

@Entity
@Table(name = "user")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@SuperBuilder
public class UserEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    @Comment("유저 이름")
    @Column(name = "user_name")
    private String userName;

    @Comment("유저 권한")
    @Column(name = "user_role")
    @Enumerated(value = EnumType.STRING)
    private UserRole userRole;

    @Comment("유저 이메일")
    @Column(name = "email")
    private String email;

    @Comment("로그인 타입 - [Email / Apple / Google]")
    @Column(name = "login_type")
    @Enumerated(value = EnumType.STRING)
    private LoginType loginType;

    @Comment("마케팅 수신 동의")
    @Column(name = "marketing_consent")
    private boolean marketingConsent;

    @Comment("푸쉬 알림 수신 동의")
    @Column(name = "push_consent")
    private boolean pushConsent;

    @Comment("FCM Token")
    @Column(name = "fcm_token")
    private String fcmToken;

    @Comment("refresh token")
    @Column(name = "refresh_token")
    private String refreshToken;

}