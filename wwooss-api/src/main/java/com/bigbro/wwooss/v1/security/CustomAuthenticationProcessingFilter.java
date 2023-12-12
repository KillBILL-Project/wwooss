package com.bigbro.wwooss.v1.security;

import com.bigbro.wwooss.v1.enumType.UserRole;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.util.StringUtils;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collection;
import java.util.List;

@Slf4j
public class CustomAuthenticationProcessingFilter extends AbstractAuthenticationProcessingFilter {

    private static final List<String> UNFILTERED_URIS = List.of("/api/v1/auth/login");
    private final TokenProvider tokenProvider;

    public CustomAuthenticationProcessingFilter(TokenProvider tokenProvider) {
        super("/v1/**");
        this.tokenProvider = tokenProvider;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        String path = request.getRequestURI();
        if (UNFILTERED_URIS.contains(path)) return new UsernamePasswordAuthenticationToken(null, null);

        String requestTokenHeader = request.getHeader("Authorization");
        if (!StringUtils.hasText(requestTokenHeader) || !requestTokenHeader.startsWith("Bearer ")) {
            throw new BadCredentialsException("인증 토큰이 발견되지 않았거나 형식이 잘못되었습니다");
        }

        String token = parseBearerToken(requestTokenHeader);
        TokenInfo tokenInfo = tokenProvider.getTokenInfo(token);

        String userEmail = tokenInfo.getUserEmail();
        Collection<? extends GrantedAuthority> authorities = getAuthorities(tokenInfo.getUserRole());

        return new UsernamePasswordAuthenticationToken(userEmail, token, authorities);
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        SecurityContextHolder.getContext().setAuthentication(authResult);
        chain.doFilter(request, response);
    }

    private String parseBearerToken(String token) {
        return token.substring(7);
    }

    private Collection<? extends GrantedAuthority> getAuthorities(UserRole userRole) {
        return List.of(new SimpleGrantedAuthority(userRole.toString()));
    }

}
