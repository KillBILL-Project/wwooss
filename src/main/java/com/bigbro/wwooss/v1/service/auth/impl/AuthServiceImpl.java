package com.bigbro.wwooss.v1.service.auth.impl;

import com.bigbro.wwooss.v1.domain.response.auth.TokenResponse;
import com.bigbro.wwooss.v1.security.TokenInfo;
import com.bigbro.wwooss.v1.security.TokenProvider;
import com.bigbro.wwooss.v1.common.WwoossResponseCode;
import com.bigbro.wwooss.v1.domain.entity.user.User;
import com.bigbro.wwooss.v1.domain.request.auth.UserExistsRequest;
import com.bigbro.wwooss.v1.domain.request.auth.UserLoginRequest;
import com.bigbro.wwooss.v1.domain.request.auth.UserRegistrationRequest;
import com.bigbro.wwooss.v1.enumType.LoginType;
import com.bigbro.wwooss.v1.exception.DataNotFoundException;
import com.bigbro.wwooss.v1.repository.user.UserRepository;
import com.bigbro.wwooss.v1.service.auth.AuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenProvider tokenProvider;

    public TokenResponse login(UserLoginRequest userLoginRequest) {

        String userEmail = userLoginRequest.getEmail();
        LoginType userLoginType = userLoginRequest.getLoginType();
        User user = userRepository.findUserByEmailAndLoginType(userEmail, userLoginType).orElseThrow(() -> new DataNotFoundException(WwoossResponseCode.NOT_FOUND_DATA, "존재하지 않는 유저입니다."));

        if (userLoginType == LoginType.EMAIL
                && (!passwordEncoder.matches(userLoginRequest.getPassword(), user.getPassword()))) {
            throw new DataNotFoundException(WwoossResponseCode.NOT_FOUND_DATA, "일치하는 유저정보가 없습니다.");
        }

        TokenResponse tokenResponse = tokenProvider.getTokenResponse(user, "init");
        user = User.of(user, tokenResponse.getRefreshToken());

        userRepository.save(user);

        return tokenResponse;
    }

    public void logout() {
        String refreshToken = (String) SecurityContextHolder.getContext().getAuthentication().getCredentials();
        TokenInfo tokenInfo = tokenProvider.getTokenInfo(refreshToken);
        User user = userRepository.findById(tokenInfo.getUserId()).orElseThrow();
        User loggedOutUser = User.of(user, null);

        userRepository.save(loggedOutUser);

    }

    public Boolean existsUser(UserExistsRequest userExistsRequest) {
        return userRepository.findUserByEmailAndLoginType(userExistsRequest.getEmail(), userExistsRequest.getLoginType()).isEmpty();
    }

    public TokenResponse register(UserRegistrationRequest userRegistrationRequest) {
        User user = User.from(userRegistrationRequest);
        TokenResponse tokenResponse = tokenProvider.getTokenResponse(user, "init");

        if (LoginType.EMAIL == user.getLoginType()) {
            String encodedPassword = passwordEncoder.encode(user.getPassword());
            user = User.of(user, encodedPassword, tokenResponse.getRefreshToken());
        } else {
            user = User.of(user, tokenResponse.getRefreshToken());
        }

        userRepository.save(user);

        return tokenResponse;
    }

    public TokenResponse reissue() {
        String refreshToken = (String) SecurityContextHolder.getContext().getAuthentication().getCredentials();

        TokenInfo tokenInfo = tokenProvider.getTokenInfo(refreshToken);
        User user = userRepository.findById(tokenInfo.getUserId()).orElseThrow();

        if (!refreshToken.equals(user.getRefreshToken())) {
            throw new IllegalStateException();
        }

        return tokenProvider.getTokenResponse(user, "reissue");
    }
}
