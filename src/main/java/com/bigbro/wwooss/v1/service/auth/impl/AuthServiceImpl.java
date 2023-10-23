package com.bigbro.wwooss.v1.service.auth.impl;

import com.bigbro.wwooss.v1.security.TokenProvider;
import com.bigbro.wwooss.v1.common.WwoossResponseCode;
import com.bigbro.wwooss.v1.domain.entity.user.User;
import com.bigbro.wwooss.v1.domain.request.auth.UserExistsRequest;
import com.bigbro.wwooss.v1.domain.request.auth.UserLoginRequest;
import com.bigbro.wwooss.v1.domain.request.auth.UserRegistrationRequest;
import com.bigbro.wwooss.v1.domain.response.auth.UserResponse;
import com.bigbro.wwooss.v1.enumType.LoginType;
import com.bigbro.wwooss.v1.exception.DataNotFoundException;
import com.bigbro.wwooss.v1.repository.user.UserRepository;
import com.bigbro.wwooss.v1.service.auth.AuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;

import static com.bigbro.wwooss.v1.security.TokenProvider.setBearerToken;

@Slf4j
@RequiredArgsConstructor
@Service
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenProvider tokenProvider;

    public UserResponse login(UserLoginRequest userLoginRequest, HttpServletResponse response) {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (principal instanceof User user) {
            user = (User) principal;
            return UserResponse.from(user);
        }

        if (userLoginRequest == null) throw new DataNotFoundException(WwoossResponseCode.NOT_FOUND_DATA, "로그인 정보가 없습니다.");


        String userEmail = userLoginRequest.getEmail();
        LoginType userLoginType = userLoginRequest.getLoginType();
        User user = userRepository.findUserByEmailAndLoginType(userEmail, userLoginType).orElseThrow(() -> new DataNotFoundException(WwoossResponseCode.NOT_FOUND_DATA, "존재하지 않는 유저입니다."));

        if (userLoginType == LoginType.EMAIL
                && (!passwordEncoder.matches(userLoginRequest.getPassword(), user.getPassword()))) {
            throw new DataNotFoundException(WwoossResponseCode.NOT_FOUND_DATA, "일치하는 유저정보가 없습니다.");
        }

        String accessToken = tokenProvider.generateAccessToken(user);
        String refreshToken = tokenProvider.generateRefreshToken(user);

        response.addHeader("Authorization", setBearerToken(accessToken));
        response.addHeader("Refresh_Token", setBearerToken(refreshToken));

        return UserResponse.from(user);
    }

    public void logout() {}

    public Boolean existsUser(UserExistsRequest userExistsRequest) {
        return userRepository.findUserByEmailAndLoginType(userExistsRequest.getEmail(), userExistsRequest.getLoginType()).isEmpty();
    }

    public UserResponse register(UserRegistrationRequest userRegistrationRequest, HttpServletResponse response) {
        User user = User.from(userRegistrationRequest);
        String accessToken = tokenProvider.generateAccessToken(user);
        String refreshToken = tokenProvider.generateRefreshToken(user);

        response.addHeader("Authorization", setBearerToken(accessToken));
        response.addHeader("Refresh_Token", setBearerToken(refreshToken));

        if (LoginType.EMAIL == user.getLoginType()) {
            String encodedPassword = passwordEncoder.encode(user.getPassword());
            user = User.of(user, encodedPassword, refreshToken);
        } else {
            user = User.of(user, refreshToken);
        }

        userRepository.save(user);

        return UserResponse.from(user);
    }
}
