package com.bigbro.wwooss.v1.entity.user;

import com.bigbro.wwooss.v1.entity.base.BaseEntity;
import com.bigbro.wwooss.v1.enumType.Gender;
import com.bigbro.wwooss.v1.enumType.LoginType;
import com.bigbro.wwooss.v1.enumType.UserRole;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.Comment;

import javax.persistence.*;

@Entity
@Table(name = "user")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@SuperBuilder
public class User extends BaseEntity {

    @Id
    @Column(name = "user_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    @Column(name = "social_id")
    private String socialId;

    @Builder.Default
    @Comment("유저 권한")
    @Column(name = "user_role")
    @Enumerated(value = EnumType.STRING)
    private UserRole userRole = UserRole.USER;

    @Comment("성별")
    @Column(name = "gender")
    @Enumerated(value = EnumType.STRING)
    private Gender gender;

    @Comment("유저 이메일")
    @Column(name = "email")
    private String email;

    @Comment("유저 패스워드")
    @Column(name = "password")
    private String password;

    @Comment("나이")
    @Column(name = "age")
    private Integer age;

    @Comment("로그인 타입 - [Email / Apple / Google]")
    @Column(name = "login_type")
    @Enumerated(value = EnumType.STRING)
    private LoginType loginType;

    @Comment("유저 국가")
    @Column(name = "country")
    private String country;

    @Comment("유저 지역")
    @Column(name = "region")
    private String region;

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

    public static User of(User user, String encodedPassword, String refreshToken) {
        return User.builder()
                .userId(user.getUserId())
                .email(user.getEmail())
                .password(encodedPassword)
                .loginType(user.getLoginType())
                .age(user.getAge())
                .gender(user.getGender())
                .country(user.getCountry())
                .region(user.getRegion())
                .refreshToken(refreshToken)
                .createdAt(user.getCreatedAt())
                .build();
    }

    public static User of(User user, String refreshToken) {
        return User.builder()
                .userId(user.getUserId())
                .email(user.getEmail())
                .password(user.getPassword())
                .loginType(user.getLoginType())
                .age(user.getAge())
                .gender(user.getGender())
                .country(user.getCountry())
                .region(user.getRegion())
                .refreshToken(refreshToken)
                .createdAt(user.getCreatedAt())
                .build();
    }

        public static User of(String email,
                                String password,
                                LoginType loginType,
                                int age, Gender gender,
                                String country,
                                String region,
                              String fcmToken,
                              String socialId) {
        return User.builder()
                .email(email)
                .password(password)
                .loginType(loginType)
                .age(age)
                .gender(gender)
                .country(country)
                .region(region)
                .socialId(socialId)
                .fcmToken(fcmToken)
                .build();
    }

    public void updatePushConsent(boolean pushConsent) {
        this.pushConsent = pushConsent;
    }

    public void updateFcmToken(String fcm) {
        this.fcmToken = fcm;
    }

    public void updateRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public void updateTokenAndPsw(String refreshToken, String password) {
        this.refreshToken = refreshToken;
        this.password = password;
    }

    public void updatePassword(String password) {
        this.password = password;
    }

}
