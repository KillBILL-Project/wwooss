package com.bigbro.wwooss.v1.domain.response.auth;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class TokenResponse {

    @Builder.Default
    private String tokenType = "bearer";
    private String accessToken;
    private Long expiresIn;
    private String refreshToken;
    private Long refreshTokenExpiresIn;
}
