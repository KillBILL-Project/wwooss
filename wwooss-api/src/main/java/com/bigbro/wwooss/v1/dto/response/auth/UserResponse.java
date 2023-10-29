package com.bigbro.wwooss.v1.dto.response.auth;

import com.bigbro.wwooss.v1.entity.user.User;
import com.bigbro.wwooss.v1.enumType.Gender;
import com.bigbro.wwooss.v1.enumType.LoginType;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class UserResponse {
    private String email;
    private LoginType loginType;
    private int age;
    private Gender gender;
    private String country;
    private String region;
    private boolean marketingConsent;
    private boolean pushConsent;

    public static UserResponse from(User user) {
        return UserResponse.builder()
                .email(user.getEmail())
                .loginType(user.getLoginType())
                .age(user.getAge())
                .gender(user.getGender())
                .country(user.getCountry())
                .region(user.getRegion())
                .marketingConsent(user.isMarketingConsent())
                .pushConsent(user.isPushConsent())
                .build();
    }

}
