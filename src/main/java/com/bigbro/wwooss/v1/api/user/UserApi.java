package com.bigbro.wwooss.v1.api.user;

import com.bigbro.wwooss.v1.common.WwoossResponse;
import com.bigbro.wwooss.v1.common.WwoossResponseUtil;
import com.bigbro.wwooss.v1.domain.request.user.UpdatePushConsentRequest;
import com.bigbro.wwooss.v1.service.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/v1/user")
@RequiredArgsConstructor
public class UserApi {
    private final UserService userService;

    @PatchMapping("/push-consent")
    public ResponseEntity<WwoossResponse<Void>> updatePushConsentStatus(@RequestBody @Valid UpdatePushConsentRequest updatePushConsentRequest) {

        // TODO : 유저 ID 넣기
        userService.updatePushConsentStatus(updatePushConsentRequest.isPushConsent(), 1L);
        return WwoossResponseUtil.responseOkNoData();
    }

}
