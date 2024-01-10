package com.bigbro.wwooss.v1.service.auth.impl;

import com.bigbro.wwooss.v1.dto.request.auth.UserExistsRequest;
import com.bigbro.wwooss.v1.dto.request.auth.UserLoginRequest;
import com.bigbro.wwooss.v1.dto.request.auth.UserRegistrationRequest;
import com.bigbro.wwooss.v1.dto.response.auth.TokenResponse;
import com.bigbro.wwooss.v1.entity.user.User;
import com.bigbro.wwooss.v1.enumType.LoginType;
import com.bigbro.wwooss.v1.exception.CustomGlobalException;
import com.bigbro.wwooss.v1.exception.DataNotFoundException;
import com.bigbro.wwooss.v1.repository.user.UserRepository;
import com.bigbro.wwooss.v1.response.WwoossResponseCode;
import com.bigbro.wwooss.v1.security.TokenInfo;
import com.bigbro.wwooss.v1.security.TokenProvider;
import com.bigbro.wwooss.v1.service.auth.AuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
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

    @Value("${gcp.web-client-id}")
    private String clientId;
    @Value("${gcp.client-secret}")
    private String clientSecret;

    private static final String TOKEN_URL = "https://oauth2.googleapis.com/token";
    private static final String REDIRECT_URI = "http://localhost:8080/callback";

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenProvider tokenProvider;
    private final RestTemplate restTemplate;

    @Transactional
    public TokenResponse login(UserLoginRequest userLoginRequest) {

        String userEmail = userLoginRequest.getEmail();
        LoginType userLoginType = userLoginRequest.getLoginType();
        User user = userRepository.findUserByEmailAndLoginType(userEmail, userLoginType).orElseThrow(() -> new DataNotFoundException(WwoossResponseCode.NOT_FOUND_DATA, "일치하는 유저정보가 없습니다."));

        if (userLoginType == LoginType.EMAIL) {
            if ((!passwordEncoder.matches(userLoginRequest.getPassword(), user.getPassword()))) {
                throw new DataNotFoundException(WwoossResponseCode.NOT_FOUND_DATA, "비밀번호가 일치하지 않습니다.");
            }
        } else if (userLoginType == LoginType.GOOGLE) {
            String authCode = userLoginRequest.getAuthCode();
            boolean isValidGoogleLogin = validateGoogleSignIn(authCode);
            if (!isValidGoogleLogin)
                throw new CustomGlobalException(WwoossResponseCode.FORBIDDEN, "구글 인증을 실패하였습니다. 다시 시도해주세요.");
        } else if (userLoginType == LoginType.APPLE) {
            throw new CustomGlobalException(WwoossResponseCode.FORBIDDEN, "애플 인증을 실패하였습니다. 다시 시도해주세요.");
        } else {
            throw new CustomGlobalException(WwoossResponseCode.FORBIDDEN, "올바른 로그인 방식이 아닙니다.");
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
        User user = User.of(
                userRegistrationRequest.getEmail(),
                userRegistrationRequest.getPassword(),
                userRegistrationRequest.getLoginType(),
                userRegistrationRequest.getAge(),
                userRegistrationRequest.getGender(),
                userRegistrationRequest.getCountry(),
                userRegistrationRequest.getRegion()
        );
        User registeredUser = userRepository.save(user);

        String accessToken = tokenProvider.generateToken(registeredUser, "access");
        String refreshToken = tokenProvider.generateToken(registeredUser, "refresh");

        if (LoginType.EMAIL == user.getLoginType()) {
            String encodedPassword = passwordEncoder.encode(user.getPassword());
            registeredUser.updateTokenAndPsw(encodedPassword, refreshToken);
        } else {
            registeredUser.updateRefreshToken(refreshToken);
        }


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
            throw new CustomGlobalException(WwoossResponseCode.UNAUTHORIZED, "올바른 인증 토큰이 아닙니다.");
        }

        String accessToken = tokenProvider.generateToken(user, "access");
        return TokenResponse.builder().accessToken(accessToken).build();
    }

    public Boolean validateGoogleSignIn(String serverAuthCode) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.add("Content-Type", "application/x-www-form-urlencoded");

            MultiValueMap<String, String> requestBody = new LinkedMultiValueMap<>();
            requestBody.add("code", serverAuthCode);
            requestBody.add("client_id", clientId);
            requestBody.add("client_secret", clientSecret);
            requestBody.add("redirect_uri", REDIRECT_URI);
            requestBody.add("grant_type", "authorization_code");

            HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(requestBody, headers);
            ResponseEntity<Void> responseEntity = restTemplate.exchange(TOKEN_URL, HttpMethod.POST, request, Void.class);

            return responseEntity.getStatusCode() == HttpStatus.OK;
        } catch (HttpClientErrorException | HttpServerErrorException | ResourceAccessException e) {
            log.error("error in validateGoogleSignIn", e);
            return false;
        }
    }
}
