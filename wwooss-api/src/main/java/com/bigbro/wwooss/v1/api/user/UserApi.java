package com.bigbro.wwooss.v1.api.user;

import com.bigbro.wwooss.v1.dto.request.user.UpdateFcmTokenRequest;
import com.bigbro.wwooss.v1.dto.request.user.UpdatePushConsentRequest;
import com.bigbro.wwooss.v1.dto.request.user.UserCredential;
import com.bigbro.wwooss.v1.dto.response.auth.UserResponse;
import com.bigbro.wwooss.v1.response.WwoossResponse;
import com.bigbro.wwooss.v1.response.WwoossResponseUtil;
import com.bigbro.wwooss.v1.service.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/v1/user")
@RequiredArgsConstructor
public class UserApi {
    private final UserService userService;

    @GetMapping
    public ResponseEntity<WwoossResponse<UserResponse>> getUserInfo() {
        return WwoossResponseUtil.responseOkAddData(userService.getUserInfo());
    }
  
    @PatchMapping("/push-consent")
    public ResponseEntity<WwoossResponse<Void>> updatePushConsentStatus(@RequestBody @Valid UpdatePushConsentRequest updatePushConsentRequest, UserCredential userCredential) {
        userService.updatePushConsentStatus(updatePushConsentRequest.isPushConsent(), userCredential.getUserId());
        return WwoossResponseUtil.responseOkNoData();
    }

    @PatchMapping("/fcm-token")
    public ResponseEntity<WwoossResponse<Void>> updateFcmToken(@RequestBody @Valid UpdateFcmTokenRequest updateFcmTokenRequest, UserCredential userCredential) {
        userService.updateFcmToken(userCredential.getUserId(), updateFcmTokenRequest.getFcmToken());
        return WwoossResponseUtil.responseOkNoData();
    }

}
