package com.bigbro.wwooss.v1.dto.request.auth;

import com.bigbro.wwooss.v1.enumType.Gender;
import com.bigbro.wwooss.v1.enumType.LoginType;
import lombok.*;

@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor
@Getter
@Builder
public class UserRegistrationRequest {
    private String email;
    private String password;
    private LoginType loginType;
    private int age;
    private Gender gender;
    private String country;
    private String region;
    private boolean pushConsent;
    private String socialToken;
    private String fcmToken;
}
