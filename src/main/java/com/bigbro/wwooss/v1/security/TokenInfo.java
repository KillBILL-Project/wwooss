package com.bigbro.wwooss.v1.security;

import io.jsonwebtoken.Claims;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Builder
@Setter
@Getter
public class TokenInfo {
    private String type;
    private Long userId;
    private Date issuedAt;
    private Date expiration;

    public static TokenInfo from(Claims claims) {
        return TokenInfo.builder()
                .type(claims.get("type", String.class))
                .userId(claims.get("userId", Long.class))
                .issuedAt(claims.getIssuedAt())
                .expiration(claims.getExpiration())
                .build();

    }
}
