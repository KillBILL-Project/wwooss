package com.bigbro.wwooss.v1.api.notification;

import com.bigbro.wwooss.v1.dto.request.auth.UserLoginRequest;
import com.bigbro.wwooss.v1.dto.request.notification.NotificationSendRequest;
import com.bigbro.wwooss.v1.dto.response.auth.TokenResponse;
import com.bigbro.wwooss.v1.entity.user.User;
import com.bigbro.wwooss.v1.enumType.NotificationTemplateCode;
import com.bigbro.wwooss.v1.repository.user.UserRepository;
import com.bigbro.wwooss.v1.response.WwoossResponse;
import com.bigbro.wwooss.v1.response.WwoossResponseUtil;
import com.bigbro.wwooss.v1.service.notification.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RequestMapping("/v1/notification")
@RestController
public class NotificationApi {

    private final NotificationService notificationService;

    private final UserRepository userRepository;

    @PostMapping("/test-push/{user-id}")
    public ResponseEntity<WwoossResponse<TokenResponse>> testPush(@PathVariable("user-id") Long userId) {
        notificationService.sendOne(NotificationSendRequest.of(List.of(userRepository.findById(userId).orElseThrow()), NotificationTemplateCode.WWOOSS_ALARM));
        return WwoossResponseUtil.responseOkNoData();
    }
}
