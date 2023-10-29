package com.bigbro.wwooss.v1.service.user.impl;

import com.bigbro.wwooss.v1.domain.entity.user.User;
import com.bigbro.wwooss.v1.domain.response.auth.UserResponse;
import com.bigbro.wwooss.v1.repository.user.UserRepository;
import com.bigbro.wwooss.v1.security.TokenInfo;
import com.bigbro.wwooss.v1.security.TokenProvider;
import com.bigbro.wwooss.v1.service.user.UserService;
import com.bigbro.wwooss.v1.common.WwoossResponseCode;
import com.bigbro.wwooss.v1.exception.DataNotFoundException;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final TokenProvider tokenProvider;

    @Override
    public UserResponse getUserInfo() {
        String token = (String) SecurityContextHolder.getContext().getAuthentication().getCredentials();
        TokenInfo tokenInfo = tokenProvider.getTokenInfo(token);

        User user = userRepository.findById(tokenInfo.getUserId()).orElseThrow();

        return UserResponse.from(user);
    }
  
    @Override
    @Transactional
    public void updatePushConsentStatus(boolean pushConsent, long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new DataNotFoundException(WwoossResponseCode.NOT_FOUND_DATA, "존재하지 않는 유저입니다."));

        user.updatePushConsent(pushConsent);
    }
}
