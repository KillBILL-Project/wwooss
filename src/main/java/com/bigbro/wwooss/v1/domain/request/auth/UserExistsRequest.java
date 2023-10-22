package com.bigbro.wwooss.v1.domain.request.auth;

import com.bigbro.wwooss.v1.enumType.LoginType;
import lombok.Getter;

@Getter
public class UserExistsRequest {
    private String email;
    private LoginType loginType;
}
