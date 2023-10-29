package com.bigbro.wwooss.v1.domain.response.auth;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class TokenResponse {
    private String accessToken;
    private String refreshToken;
}
