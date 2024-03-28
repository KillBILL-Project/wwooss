package com.bigbro.wwooss.v1.security;

import com.bigbro.wwooss.v1.entity.user.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Slf4j
@Component
public class TokenProvider implements InitializingBean {

    @Value("${jwt.secret}")
    private String secret;
    @Value("${jwt.access-token-validity-in-milliseconds}")
    private long accessTokenValidityInMilliseconds;
    @Value("${jwt.refresh-token-validity-in-milliseconds}")
    private long refreshTokenValidityInMilliseconds;

    private Key key;

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
                .signWith(key, SignatureAlgorithm.HS384)
                .compact();
    }

    public TokenInfo getTokenInfo(String token) throws BadCredentialsException {
        try {
            Claims claims = getClaims(token);
            return TokenInfo.from(claims);
        } catch (ExpiredJwtException exception) {
            throw new BadCredentialsException("인증 토큰이 만료되었습니다.");
        }
    }

    private Claims getClaims(String token) throws ExpiredJwtException {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}
