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
@Table(name = "withdrawal_user")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SuperBuilder
@Getter
public class WithdrawalUser extends BaseEntity {

    @Id
    @Column(name = "withdrawal_user_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long withdrawalUserId;

    @Column(name = "user_id")
    @Comment("유저 ID")
    private Long userId;

    @Comment("유저 권한")
    @Column(name = "user_role")
    @Enumerated(value = EnumType.STRING)
    private UserRole userRole;

    @Comment("성별")
    @Column(name = "gender")
    @Enumerated(value = EnumType.STRING)
    private Gender gender;

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

    public static WithdrawalUser of(Long userId, UserRole userRole, Gender gender, Integer age, LoginType loginType,
                                    String country, String region) {
        return WithdrawalUser.builder()
                .userId(userId)
                .userRole(userRole)
                .gender(gender)
                .age(age)
                .loginType(loginType)
                .country(country)
                .region(region)
                .build();
    }

}
