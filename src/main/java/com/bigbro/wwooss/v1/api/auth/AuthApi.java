package com.bigbro.wwooss.v1.api.auth;

import com.bigbro.wwooss.v1.common.WwoossResponse;
import com.bigbro.wwooss.v1.common.WwoossResponseUtil;
import com.bigbro.wwooss.v1.domain.request.auth.UserExistsRequest;
import com.bigbro.wwooss.v1.domain.request.auth.UserLoginRequest;
import com.bigbro.wwooss.v1.domain.request.auth.UserRegistrationRequest;
import com.bigbro.wwooss.v1.domain.response.auth.TokenResponse;
import com.bigbro.wwooss.v1.service.auth.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RequestMapping("/v1/auth")
@RestController
public class AuthApi {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<WwoossResponse<TokenResponse>> login(@RequestBody UserLoginRequest userLoginRequest) {
        return WwoossResponseUtil.responseOkAddData(authService.login(userLoginRequest));
    }

    @PostMapping("/reissue")
    public ResponseEntity<WwoossResponse<TokenResponse>> reissue() {
        return WwoossResponseUtil.responseOkAddData(authService.reissue());
    }

    @PostMapping("/logout")
    public ResponseEntity<WwoossResponse<Void>> logout() {
        authService.logout();
        return WwoossResponseUtil.responseOkNoData();
    }

    @PostMapping("/register")
    public ResponseEntity<WwoossResponse<TokenResponse>> registerUser(@RequestBody UserRegistrationRequest userRegistrationRequest) {
        return WwoossResponseUtil.responseOkAddData(authService.register(userRegistrationRequest));
    }

    @PostMapping("/exist")
    public ResponseEntity<WwoossResponse<Boolean>> checkExistence(@RequestBody UserExistsRequest userExistsRequest) {
        return WwoossResponseUtil.responseOkAddData(authService.existsUser(userExistsRequest));
    }

}
