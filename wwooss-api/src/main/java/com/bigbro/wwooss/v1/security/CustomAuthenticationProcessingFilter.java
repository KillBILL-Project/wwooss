package com.bigbro.wwooss.v1.security;

import com.bigbro.wwooss.v1.enumType.UserRole;
import lombok.extern.slf4j.Slf4j;
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

    private final TokenProvider tokenProvider;

    public CustomAuthenticationProcessingFilter(TokenProvider tokenProvider) {
        super("/v1/**");
        this.tokenProvider = tokenProvider;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException, IOException {
        String requestTokenHeader = request.getHeader("Authorization");
        if (!StringUtils.hasText(requestTokenHeader)) return new UsernamePasswordAuthenticationToken(null, null);

        if (!requestTokenHeader.startsWith("Bearer ")) {
            return setUnauthorizedUser(response);
        }

        String token = parseBearerToken(requestTokenHeader);
        TokenInfo tokenInfo = tokenProvider.getTokenInfo(token);

        if (tokenProvider.isTokenExpired(token)) {
            return setUnauthorizedUser(response);
        }

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

    private Authentication setUnauthorizedUser(HttpServletResponse response) throws IOException {
        response.sendError(401);
        return new UsernamePasswordAuthenticationToken(null, null);
    }

}
