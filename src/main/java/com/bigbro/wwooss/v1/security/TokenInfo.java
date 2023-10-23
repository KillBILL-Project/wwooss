package com.bigbro.wwooss.v1.security;

import com.bigbro.wwooss.v1.enumType.UserRole;
import io.jsonwebtoken.Claims;
import lombok.Builder;
import lombok.Getter;

import java.util.Date;

@Builder
@Getter
public class TokenInfo {
    private UserRole userRole;
    private Long userId;
    private String userEmail;
    private Date issuedAt;
    private Date expiration;

    public static TokenInfo from(Claims claims) {
        return TokenInfo.builder()
                .userRole(UserRole.valueOf(claims.get("userRole", String.class)))
                .userId(claims.get("userId", Long.class))
                .userEmail(claims.get("userEmail", String.class))
                .issuedAt(claims.getIssuedAt())
                .expiration(claims.getExpiration())
                .build();

    }
}
