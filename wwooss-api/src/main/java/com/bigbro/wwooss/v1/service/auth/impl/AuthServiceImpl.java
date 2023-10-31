package com.bigbro.wwooss.v1.service.auth.impl;

import com.bigbro.wwooss.v1.dto.request.auth.UserExistsRequest;
import com.bigbro.wwooss.v1.dto.request.auth.UserLoginRequest;
import com.bigbro.wwooss.v1.dto.request.auth.UserRegistrationRequest;
import com.bigbro.wwooss.v1.dto.response.auth.TokenResponse;
import com.bigbro.wwooss.v1.entity.user.User;
import com.bigbro.wwooss.v1.enumType.LoginType;
import com.bigbro.wwooss.v1.exception.DataNotFoundException;
import com.bigbro.wwooss.v1.repository.user.UserRepository;
import com.bigbro.wwooss.v1.response.WwoossResponseCode;
import com.bigbro.wwooss.v1.security.TokenInfo;
import com.bigbro.wwooss.v1.security.TokenProvider;
import com.bigbro.wwooss.v1.service.auth.AuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import javax.transaction.Transactional;

@Slf4j
@RequiredArgsConstructor
@Service
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenProvider tokenProvider;
    private final RestTemplate restTemplate;

    @Transactional
    public TokenResponse login(UserLoginRequest userLoginRequest) {

        String userEmail = userLoginRequest.getEmail();
        LoginType userLoginType = userLoginRequest.getLoginType();
        User user = userRepository.findUserByEmailAndLoginType(userEmail, userLoginType).orElseThrow(() -> new DataNotFoundException(WwoossResponseCode.NOT_FOUND_DATA, "존재하지 않는 유저입니다."));

        if (userLoginType == LoginType.EMAIL
                && (!passwordEncoder.matches(userLoginRequest.getPassword(), user.getPassword()))) {
            throw new DataNotFoundException(WwoossResponseCode.NOT_FOUND_DATA, "일치하는 유저정보가 없습니다.");
        } else if ( userLoginType == LoginType.GOOGLE) {
            String serverAuthCode = userLoginRequest.getGoogleAuthCode();
            boolean isValidGoogleLogin = validateGoogleSignIn(serverAuthCode);

        }

        String accessToken = tokenProvider.generateToken(user, "access");
        String refreshToken = tokenProvider.generateToken(user, "refresh");
        user = User.of(user, refreshToken);

        userRepository.save(user);

        return TokenResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    @Transactional
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

    @Transactional
    public TokenResponse register(UserRegistrationRequest userRegistrationRequest) {
        User user = User.of(userRegistrationRequest.getEmail(),
                userRegistrationRequest.getEmail(),
                userRegistrationRequest.getLoginType(),
                userRegistrationRequest.getAge(),
                userRegistrationRequest.getGender(),
                userRegistrationRequest.getCountry(),
                userRegistrationRequest.getRegion());

        String accessToken = tokenProvider.generateToken(user, "access");
        String refreshToken = tokenProvider.generateToken(user, "refresh");

        if (LoginType.EMAIL == user.getLoginType()) {
            String encodedPassword = passwordEncoder.encode(user.getPassword());
            user = User.of(user, encodedPassword, refreshToken);
        } else {
            user = User.of(user, refreshToken);
        }

        userRepository.save(user);

        return TokenResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    public TokenResponse reissue() {
        String refreshToken = (String) SecurityContextHolder.getContext().getAuthentication().getCredentials();

        TokenInfo tokenInfo = tokenProvider.getTokenInfo(refreshToken);
        User user = userRepository.findById(tokenInfo.getUserId()).orElseThrow();

        if (!refreshToken.equals(user.getRefreshToken())) {
            throw new IllegalStateException();
        }

        String accessToken = tokenProvider.generateToken(user, "access");

        return TokenResponse.builder().accessToken(accessToken).build();
    }

    private static final String TOKEN_URL = "https://oauth2.googleapis.com/token";
    private static final String CLIENT_ID = "1361813122-mn0eqsjcn0aar3cvr8on3grfo7agfi0h.apps.googleusercontent.com";
    private static final String CLIENT_SECRET = "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";
    private static final String REDIRECT_URI = "http://localhost:8080/callback";

    public Boolean validateGoogleSignIn(String serverAuthCode) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.add("Content-Type", "application/x-www-form-urlencoded");

            MultiValueMap<String, String> requestBody = new LinkedMultiValueMap<>();
            requestBody.add("code", serverAuthCode);
            requestBody.add("client_id", CLIENT_ID);
            requestBody.add("client_secret", CLIENT_SECRET);
            requestBody.add("redirect_uri", REDIRECT_URI);
            requestBody.add("grant_type", "authorization_code");

            HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(requestBody, headers);
            ResponseEntity<Void> responseEntity = restTemplate.exchange(TOKEN_URL, HttpMethod.POST, request, Void.class);

            return responseEntity.getStatusCode() == HttpStatus.OK;
        } catch (HttpClientErrorException | HttpServerErrorException | ResourceAccessException e) {
            return false;
        }
    }
}
