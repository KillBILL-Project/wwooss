package com.bigbro.wwooss.v1.common;

import com.bigbro.wwooss.v1.dto.request.user.UserCredential;
import com.bigbro.wwooss.v1.security.TokenInfo;
import java.util.Objects;
import javax.servlet.http.HttpServletRequest;
import org.springframework.core.MethodParameter;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

public class UserArgumentResolver implements HandlerMethodArgumentResolver {

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.getParameterType().equals(UserCredential.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
                                  NativeWebRequest webRequest, WebDataBinderFactory binderFactory) {

        HttpServletRequest request = webRequest.getNativeRequest(HttpServletRequest.class);

        if (Objects.isNull(request)) {
            throw new RuntimeException("Servlet Request Not Found");
        }

        TokenInfo tokenInfo = (TokenInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return UserCredential.builder()
                .userId(tokenInfo.getUserId())
                .userRole(tokenInfo.getUserRole())
                .userEmail(tokenInfo.getUserEmail())
                .build();
    }

}