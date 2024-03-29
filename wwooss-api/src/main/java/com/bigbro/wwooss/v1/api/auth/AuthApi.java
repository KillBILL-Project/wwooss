package com.bigbro.wwooss.v1.api.auth;

import com.bigbro.wwooss.v1.dto.request.auth.*;
import com.bigbro.wwooss.v1.dto.request.notification.NotificationSendRequest;
import com.bigbro.wwooss.v1.dto.request.user.UserCredential;
import com.bigbro.wwooss.v1.dto.response.auth.TokenResponse;
import com.bigbro.wwooss.v1.response.WwoossResponse;
import com.bigbro.wwooss.v1.response.WwoossResponseUtil;
import com.bigbro.wwooss.v1.service.auth.AuthService;
import com.bigbro.wwooss.v1.service.notification.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RequiredArgsConstructor
@RequestMapping("/v1/auth")
@RestController
public class AuthApi {

    private final AuthService authService;

    private final NotificationService notificationService;

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

    @DeleteMapping("/withdrawal")
    public ResponseEntity<WwoossResponse<Void>> withdrawal(UserCredential userCredential) {
        authService.withdrawalUser(userCredential.getUserId());
        return WwoossResponseUtil.responseOkNoData();
    }

    @PatchMapping("/reset-password")
    public ResponseEntity<WwoossResponse<Void>> resetPassword(@RequestBody @Valid ResetPasswordRequest resetPasswordRequest) {
        authService.resetPassword(resetPasswordRequest.getEmail());
        return WwoossResponseUtil.responseOkNoData();
    }

    @PatchMapping("/change-password")
    public ResponseEntity<WwoossResponse<Void>> changePassword(@RequestBody @Valid  ChangePasswordRequest changePasswordRequest, UserCredential userCredential) {
        authService.changePassword(userCredential.getUserId(), changePasswordRequest.getPassword());
        return WwoossResponseUtil.responseOkNoData();
    }

}
