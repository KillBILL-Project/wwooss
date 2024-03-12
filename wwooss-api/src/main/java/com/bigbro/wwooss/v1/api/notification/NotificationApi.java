package com.bigbro.wwooss.v1.api.notification;

import com.bigbro.wwooss.v1.dto.request.notification.NotificationSendRequest;
import com.bigbro.wwooss.v1.dto.request.user.UserCredential;
import com.bigbro.wwooss.v1.dto.response.auth.TokenResponse;
import com.bigbro.wwooss.v1.dto.response.notification.NotificationListResponse;
import com.bigbro.wwooss.v1.enumType.NotificationTemplateCode;
import com.bigbro.wwooss.v1.repository.user.UserRepository;
import com.bigbro.wwooss.v1.response.WwoossResponse;
import com.bigbro.wwooss.v1.response.WwoossResponseUtil;
import com.bigbro.wwooss.v1.service.notification.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
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

    @GetMapping
    public ResponseEntity<WwoossResponse<NotificationListResponse>> getNotificationList(UserCredential userCredential, @PageableDefault(size = 10, sort = "createdAt", direction = Sort.Direction.DESC, page = 0) Pageable pageable) {
        return WwoossResponseUtil.responseOkAddData(notificationService.getNotificationList(userCredential.getUserId(), pageable));
    }

}
