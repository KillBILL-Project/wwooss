package com.bigbro.wwooss.v1.security;

import com.bigbro.wwooss.v1.common.WwoossResponseCode;
import com.bigbro.wwooss.v1.domain.entity.user.User;
import com.bigbro.wwooss.v1.enumType.UserRole;
import com.bigbro.wwooss.v1.exception.DataNotFoundException;
import com.bigbro.wwooss.v1.repository.user.UserRepository;
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

import static com.bigbro.wwooss.v1.security.TokenProvider.setBearerToken;

@Slf4j
public class CustomAuthenticationProcessingFilter extends AbstractAuthenticationProcessingFilter {

    private final TokenProvider tokenProvider;
    private final UserRepository userRepository;

    public CustomAuthenticationProcessingFilter(UserRepository userRepository, TokenProvider tokenProvider) {
        super("/v1/**");
        this.userRepository = userRepository;
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
        User user = userRepository.findById(tokenInfo.getUserId()).orElseThrow(() -> new DataNotFoundException(WwoossResponseCode.NOT_FOUND_DATA, "존재하지 않는 유저입니다."));
        Collection<? extends GrantedAuthority> authorities = getAuthorities(user.getUserRole());

        String type = tokenInfo.getType();

        if (tokenProvider.isTokenExpired(token)) {
            setUnauthorizedUser(response);
        }

        if (type.equals("access")) {
            return new UsernamePasswordAuthenticationToken(user, token, authorities);
        } else if ("refresh".equals(type)) {
            String accessToken = tokenProvider.generateAccessToken(user);
            response.addHeader("Authorization", setBearerToken(accessToken));
            return new UsernamePasswordAuthenticationToken(user, accessToken, authorities);
        }

        return setUnauthorizedUser(response);
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        SecurityContextHolder.getContext().setAuthentication(authResult);
        chain.doFilter(request, response);
    }

    private String parseBearerToken(String token) {
        return token.substring(7);
    }

    private Collection<? extends GrantedAuthority> getAuthorities(UserRole role) {
        return List.of(new SimpleGrantedAuthority(role.toString()));
    }

    private Authentication setUnauthorizedUser(HttpServletResponse response) throws IOException {
        response.sendError(401);
        return new UsernamePasswordAuthenticationToken(null, null);
    }

}
