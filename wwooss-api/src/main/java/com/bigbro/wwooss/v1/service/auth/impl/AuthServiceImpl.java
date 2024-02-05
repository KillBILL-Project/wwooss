package com.bigbro.wwooss.v1.service.auth.impl;

import com.bigbro.wwooss.v1.dto.request.auth.UserExistsRequest;
import com.bigbro.wwooss.v1.dto.request.auth.UserLoginRequest;
import com.bigbro.wwooss.v1.dto.request.auth.UserRegistrationRequest;
import com.bigbro.wwooss.v1.dto.response.auth.TokenResponse;
import com.bigbro.wwooss.v1.entity.user.User;
import com.bigbro.wwooss.v1.enumType.LoginType;
import com.bigbro.wwooss.v1.exception.CustomGlobalException;
import com.bigbro.wwooss.v1.exception.DataNotFoundException;
import com.bigbro.wwooss.v1.exception.IncorrectDataException;
import com.bigbro.wwooss.v1.repository.user.UserRepository;
import com.bigbro.wwooss.v1.response.WwoossResponseCode;
import com.bigbro.wwooss.v1.security.TokenInfo;
import com.bigbro.wwooss.v1.security.TokenProvider;
import com.bigbro.wwooss.v1.service.auth.AuthService;
import com.google.gson.*;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
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
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigInteger;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.spec.RSAPublicKeySpec;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.Objects;

import static com.bigbro.wwooss.v1.response.WwoossResponseCode.INVALID_TOKEN;

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

    private static final String GOOGLE_ID_PREFIX = "GOOGLE_";
    private static final String IOS_ID_PREFIX = "APPLE_";

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenProvider tokenProvider;
    private final RestTemplate restTemplate;



    @Transactional
    public TokenResponse login(UserLoginRequest userLoginRequest) {

        String userEmail = userLoginRequest.getEmail();
        LoginType userLoginType = userLoginRequest.getLoginType();
        User user;
        String authCode = userLoginRequest.getAuthCode();


        if (userLoginType == LoginType.EMAIL) {
            user = userRepository.findUserByEmailAndLoginType(userEmail, userLoginType).orElseThrow(() ->
                    new DataNotFoundException(WwoossResponseCode.NOT_FOUND_DATA, "일치하는 유저정보가 없습니다."));
            if ((!passwordEncoder.matches(userLoginRequest.getPassword(), user.getPassword()))) {
                throw new DataNotFoundException(WwoossResponseCode.NOT_FOUND_DATA, "비밀번호가 일치하지 않습니다.");
            }
        } else if (userLoginType == LoginType.GOOGLE) {
            String socialId = parseGoogleToken(authCode);
            user = userRepository.findUserBySocialIdAndLoginType(socialId, userLoginType).orElseThrow(() ->
                    new DataNotFoundException(WwoossResponseCode.NOT_FOUND_DATA, "일치하는 유저정보가 없습니다."));

        } else if (userLoginType == LoginType.APPLE) {
            String socialId = parseAppleToken(authCode);
            user = userRepository.findUserBySocialIdAndLoginType(socialId, userLoginType).orElseThrow(() ->
                    new DataNotFoundException(WwoossResponseCode.NOT_FOUND_DATA, "일치하는 유저정보가 없습니다."));
        } else {
            throw new CustomGlobalException(WwoossResponseCode.FORBIDDEN, "올바른 로그인 방식이 아닙니다.");
        }

        String accessToken = tokenProvider.generateToken(user, "access");
        String refreshToken = tokenProvider.generateToken(user, "refresh");
        user.updateRefreshToken(refreshToken);

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

        String encodedPassword = null;
        String socialId = null;
        if (LoginType.EMAIL.equals(userRegistrationRequest.getLoginType())) {
            encodedPassword = passwordEncoder.encode(userRegistrationRequest.getPassword());
        } else if(LoginType.APPLE.equals(userRegistrationRequest.getLoginType())){
            socialId = parseAppleToken(userRegistrationRequest.getSocialToken());
        } else {
            socialId = parseGoogleToken(userRegistrationRequest.getSocialToken());
        }

        User user = User.of(
                userRegistrationRequest.getEmail(),
                encodedPassword,
                userRegistrationRequest.getLoginType(),
                userRegistrationRequest.getAge(),
                userRegistrationRequest.getGender(),
                userRegistrationRequest.getCountry(),
                userRegistrationRequest.getRegion(),
                socialId
        );
        User registeredUser = userRepository.save(user);

        String accessToken = tokenProvider.generateToken(registeredUser, "access");
        String refreshToken = tokenProvider.generateToken(registeredUser, "refresh");
        user.updateRefreshToken(refreshToken);


        return TokenResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    /**
     * google identity token parsing
     */
    public String parseGoogleToken(String identityToken) {
        StringBuilder result = new StringBuilder();

        try {
            URL url = new URL("https://www.googleapis.com/oauth2/v1/tokeninfo?id_token="+identityToken);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");

            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line = "";

            while ((line = br.readLine()) != null) {
                result.append(line);
            }
        } catch (IOException e) {
            throw new IncorrectDataException(INVALID_TOKEN);
        }

        JsonObject data = (JsonObject) JsonParser.parseString(result.toString());

        // 쌍따움표 제거 : ""abc""이와 같이 리턴됨
        String removedDoubleQuotes = data.get("user_id").toString().substring(1, data.get("user_id").toString().length() - 1);
        return GOOGLE_ID_PREFIX + removedDoubleQuotes;
    }

    /**
     * apple identity token parsing
     */
    private String parseAppleToken(String identityToken) {
       String iosPublicKey = iosPublicKey();
        JsonObject availableObject = getAvailableObject(iosPublicKey, identityToken);

        if(Objects.isNull(availableObject)) {
            throw new IncorrectDataException(INVALID_TOKEN, "available Object is Null");
        }

        PublicKey publicKey = getPublicKey(availableObject);
        Claims userInfo = Jwts.parser().setSigningKey(publicKey).parseClaimsJws(identityToken).getBody();

        JsonObject userInfoObject = (JsonObject) JsonParser.parseString(new Gson().toJson(userInfo));
        JsonElement appleAlg = userInfoObject.get("sub");

        return IOS_ID_PREFIX + appleAlg.getAsString();
    }

    private JsonObject getAvailableObject(String publicKey, String identityToken) {
        // 클라이언트로부터 받은 identity token decode
        JsonObject keys = (JsonObject) JsonParser.parseString(publicKey);
        JsonArray keyArray = (JsonArray) keys.get("keys");

        String[] decodeArray = identityToken.split("\\.");
        String header = new String(Base64.getDecoder().decode(decodeArray[0]));

        // TODO : response dto, compareTo, equals, stream
        // apple에서 제공해주는 kid, alg 일치 확인
        // kid : key ID - key rolling이 일어나는 시점에는 여러개의 key들이 동시에 존재할 수 있음. 이를 구분하기 위해 필요
        // alg : algorithm - 어떤 알고리즘을 적용하는지
        JsonElement kid = ((JsonObject) JsonParser.parseString(header)).get("kid");
        JsonElement alg = ((JsonObject) JsonParser.parseString(header)).get("alg");

        // apple에서 받아온 공개키랑 비교
        JsonObject availableObject = null;
        for(int i = 0; i < keyArray.size(); i++) {
            JsonObject appleObject = (JsonObject) keyArray.get(i);
            JsonElement appleKid = appleObject.get("kid");
            JsonElement appleAlg = appleObject.get("alg");

            if(Objects.equals(appleKid, kid) && Objects.equals(appleAlg, alg)) {
                availableObject = appleObject;
                break;
            }
        }

        return availableObject;
    }

    private String iosPublicKey() {
        StringBuilder sb = new StringBuilder();
        // 3개의 공개키
        try {
            URL url = new URL("https://appleid.apple.com/auth/keys");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String line = "";
            while((line = br.readLine()) != null) {
                sb.append(line);
            }
            return sb.toString();
        } catch (IOException e) {
            throw new IncorrectDataException(INVALID_TOKEN);
        }
    }

    private PublicKey getPublicKey(JsonObject object) {
        String nStr = object.get("n").toString();
        String eStr = object.get("e").toString();

        byte[] nBytes = Base64.getUrlDecoder().decode(nStr.substring(1, nStr.length() - 1));
        byte[] eBytes = Base64.getUrlDecoder().decode(eStr.substring(1, eStr.length() - 1));

        BigInteger n = new BigInteger(1, nBytes);
        BigInteger e = new BigInteger(1, eBytes);

        try {
            RSAPublicKeySpec publicKeySpec = new RSAPublicKeySpec(n, e);
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            PublicKey publicKey = keyFactory.generatePublic(publicKeySpec);
            return publicKey;
        } catch (Exception exception) {
            throw new IncorrectDataException(INVALID_TOKEN);
        }
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

    @Deprecated
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
