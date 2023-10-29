package com.bigbro.wwooss.v1.domain.request.auth;

import com.bigbro.wwooss.v1.enumType.LoginType;
import lombok.Getter;

@Getter
public class UserLoginRequest {
    private LoginType loginType;
    private String email;
    private String password;
    private String googleAuthCode;
}
