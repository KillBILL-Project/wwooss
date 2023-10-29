package com.bigbro.wwooss.v1.domain.request.auth;

import com.bigbro.wwooss.v1.enumType.Gender;
import com.bigbro.wwooss.v1.enumType.LoginType;
import lombok.Getter;

@Getter
public class UserRegistrationRequest {
    private String email;
    private String password;
    private LoginType loginType;
    private int age;
    private Gender gender;
    private String country;
    private String region;
    private boolean pushConsent;
}
