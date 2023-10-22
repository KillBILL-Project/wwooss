package com.bigbro.wwooss.v1.api.auth;

import com.bigbro.wwooss.v1.common.WwoossResponse;
import com.bigbro.wwooss.v1.common.WwoossResponseUtil;
import com.bigbro.wwooss.v1.domain.request.auth.UserExistsRequest;
import com.bigbro.wwooss.v1.domain.request.auth.UserLoginRequest;
import com.bigbro.wwooss.v1.domain.request.auth.UserRegistrationRequest;
import com.bigbro.wwooss.v1.domain.response.auth.UserResponse;
import com.bigbro.wwooss.v1.service.auth.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;

@RequiredArgsConstructor
@RequestMapping("/v1/auth")
@RestController
public class AuthApi {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<WwoossResponse<UserResponse>> login(@RequestBody(required = false) UserLoginRequest userLoginRequest, HttpServletResponse response) {
        return WwoossResponseUtil.responseOkAddData(authService.login(userLoginRequest, response));
    }

    @PostMapping("/logout")
    public ResponseEntity<WwoossResponse<Void>> logout() {
        return WwoossResponseUtil.responseOkNoData();
    }

    @PostMapping("/register")
    public ResponseEntity<WwoossResponse<UserResponse>> registerUser(@RequestBody UserRegistrationRequest userRegistrationRequest, HttpServletResponse response) {
        return WwoossResponseUtil.responseOkAddData(authService.register(userRegistrationRequest, response));
    }

    @PostMapping("/exist")
    public ResponseEntity<WwoossResponse<Boolean>> checkExistence(@RequestBody UserExistsRequest userExistsRequest) {
        return WwoossResponseUtil.responseOkAddData(authService.existsUser(userExistsRequest));
    }
}
