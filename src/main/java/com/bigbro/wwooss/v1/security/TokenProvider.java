package com.bigbro.wwooss.v1.security;

import com.bigbro.wwooss.v1.domain.entity.user.User;
import com.bigbro.wwooss.v1.domain.response.auth.TokenResponse;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Slf4j
@Component
public class TokenProvider implements InitializingBean {

    private final String secret;
    private final long accessTokenValidityInMilliseconds;
    private final long refreshTokenValidityInMilliseconds;

    private Key key;

    public TokenProvider(
            @Value("${jwt.secret}") String secret,
            @Value("${jwt.access-token-validity-in-milliseconds}") long accessTokenValidityInMilliseconds,
            @Value("${jwt.refresh-token-validity-in-milliseconds}") long refreshTokenValidityInMilliseconds) {
        this.secret = secret;
        this.accessTokenValidityInMilliseconds = accessTokenValidityInMilliseconds;
        this.refreshTokenValidityInMilliseconds = refreshTokenValidityInMilliseconds;
    }

    @Override
    public void afterPropertiesSet() {
        byte[] keyBytes = Decoders.BASE64.decode(secret);
        this.key = Keys.hmacShaKeyFor(keyBytes);
    }

    public String generateToken(User user, String tokenType) {
        Date expiration = "access".equals(tokenType)
                ? new Date(System.currentTimeMillis() + this.accessTokenValidityInMilliseconds)
                : new Date(System.currentTimeMillis() + this.refreshTokenValidityInMilliseconds);

        return Jwts.builder()
                .claim("userId", user.getUserId())
                .claim("userEmail", user.getEmail())
                .claim("userRole", user.getUserRole())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(expiration)
                .signWith(key, SignatureAlgorithm.HS512)
                .compact();
    }

    public TokenResponse getTokenResponse(User user, String type) {
        String accessToken = generateToken(user, "access");
        String refreshToken = "init".equals(type) ? generateToken(user, "refresh") : null;
        Long refreshTokenExpiresIn = "init".equals(type) ? refreshTokenValidityInMilliseconds : null;
        return TokenResponse.builder()
                .accessToken(accessToken)
                .expiresIn(accessTokenValidityInMilliseconds)
                .refreshToken(refreshToken)
                .refreshTokenExpiresIn(refreshTokenExpiresIn)
                .build();
    }

    public TokenInfo getTokenInfo(String token) {
        Claims claims = getClaims(token);
        return TokenInfo.from(claims);
    }

    public boolean isTokenExpired(String token) {
        Claims claims = getClaims(token);
        Date expiration = claims.getExpiration();
        return expiration.before(new Date());
    }

    private Claims getClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public static String setBearerToken(String token) {
        return "Bearer " + token;
    }

}
