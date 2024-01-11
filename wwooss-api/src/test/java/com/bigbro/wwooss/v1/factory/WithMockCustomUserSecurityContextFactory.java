package com.bigbro.wwooss.v1.factory;

import com.bigbro.wwooss.v1.annotation.WithMockCustomUser;
import com.bigbro.wwooss.v1.enumType.UserRole;
import com.bigbro.wwooss.v1.security.TokenInfo;
import java.util.Collection;
import java.util.List;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithSecurityContextFactory;

public class WithMockCustomUserSecurityContextFactory implements WithSecurityContextFactory<WithMockCustomUser> {

    @Override
    public SecurityContext createSecurityContext(WithMockCustomUser annotation) {
        final SecurityContext context = SecurityContextHolder.createEmptyContext();
        TokenInfo tokenInfo =
                TokenInfo.builder()
                        .userId(Long.parseLong(annotation.userId()))
                        .userEmail(annotation.userEmail())
                        .userRole(annotation.userRole())
                        .build();
        Collection<? extends GrantedAuthority> authorities = getAuthorities(tokenInfo.getUserRole());
        final Authentication auth = new UsernamePasswordAuthenticationToken(tokenInfo, null, authorities);
        context.setAuthentication(auth);
        return context;
    }

    private Collection<? extends GrantedAuthority> getAuthorities(UserRole userRole) {
        return List.of(new SimpleGrantedAuthority("ROLE_" + userRole.toString()));
    }
}
