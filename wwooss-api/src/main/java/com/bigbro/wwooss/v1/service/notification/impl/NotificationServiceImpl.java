package com.bigbro.wwooss.v1.service.notification.impl;

import com.bigbro.wwooss.v1.dto.request.notification.NotificationSendRequest;
import com.bigbro.wwooss.v1.entity.notification.NotificationTemplate;
import com.bigbro.wwooss.v1.repository.notification.NotificationRepository;
import com.bigbro.wwooss.v1.repository.notification.NotificationTemplateRepository;
import com.bigbro.wwooss.v1.service.notification.FirebaseService;
import com.bigbro.wwooss.v1.service.notification.NotificationService;
import java.util.Objects;
import java.util.Optional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
@Slf4j
public class NotificationServiceImpl implements NotificationService {


    private final NotificationRepository notificationRepository;

    private final NotificationTemplateRepository notificationTemplateRepository;

    private final FirebaseService firebaseService;

    @Override
    @Transactional
    public void sendOne(NotificationSendRequest notificationSendRequest) {
        if (notificationSendRequest.isEmptyTargets()) {
            log.error("notification no target");
            return;
        }

        NotificationTemplate notificationTemplate = notificationTemplateRepository.findByTemplateCode(
                notificationSendRequest.getTemplateCode());
        if(Objects.isNull(notificationTemplate)) {
            log.error("notification no template");
            return;
        }

        // 단일 발송 이기 때문에 무조건 첫번째만
        firebaseService.sendOne(notificationTemplate, notificationSendRequest.getVariableMap(),
                notificationSendRequest.getTargets().get(0).getFcmToken());
    }

    @Override
    @Transactional
    public void sendMany(NotificationSendRequest notificationSendRequest) {
        if (notificationSendRequest.isEmptyTargets()) {
            log.info("notification no target");
            return;
        }
        NotificationTemplate notificationTemplate = notificationTemplateRepository.findByTemplateCode(
                notificationSendRequest.getTemplateCode());
    }
}
