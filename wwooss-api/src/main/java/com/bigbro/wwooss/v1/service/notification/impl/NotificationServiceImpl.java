package com.bigbro.wwooss.v1.service.notification.impl;

import com.bigbro.wwooss.v1.dto.request.notification.NotificationSendRequest;
import com.bigbro.wwooss.v1.dto.response.notification.NotificationListResponse;
import com.bigbro.wwooss.v1.dto.response.notification.NotificationResponse;
import com.bigbro.wwooss.v1.entity.notification.Notification;
import com.bigbro.wwooss.v1.entity.notification.NotificationTemplate;
import com.bigbro.wwooss.v1.entity.user.User;
import com.bigbro.wwooss.v1.exception.DataNotFoundException;
import com.bigbro.wwooss.v1.repository.notification.NotificationRepository;
import com.bigbro.wwooss.v1.repository.notification.NotificationTemplateRepository;
import com.bigbro.wwooss.v1.repository.user.UserRepository;
import com.bigbro.wwooss.v1.response.WwoossResponseCode;
import com.bigbro.wwooss.v1.service.notification.FirebaseService;
import com.bigbro.wwooss.v1.service.notification.NotificationService;
import com.google.firebase.messaging.FirebaseMessagingException;

import org.springframework.data.domain.Pageable;
import java.util.List;
import java.util.Objects;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
@Slf4j
public class NotificationServiceImpl implements NotificationService {


    private final NotificationRepository notificationRepository;

    private final NotificationTemplateRepository notificationTemplateRepository;

    private final FirebaseService firebaseService;

    private final UserRepository userRepository;

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


        try {
            // 단일 발송 이기 때문에 무조건 첫번째만
            firebaseService.sendOne(notificationTemplate, notificationSendRequest.getVariableMap(),
                    notificationSendRequest.getTargets().get(0).getFcmToken());

            notificationRepository.save(Notification.of(notificationSendRequest.getTargets().get(0)
            ,notificationTemplate, true));
        } catch (FirebaseMessagingException fe) {
            log.error(fe.getMessage(), fe);
            notificationRepository.save(Notification.failOf(notificationSendRequest.getTargets().get(0),
                    fe.getMessage(), notificationTemplate.getTemplateCode()));
        }

    }

    @Override
    @Transactional
    public void sendMany(NotificationSendRequest notificationSendRequest) {
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

        try {
            firebaseService.sendMany(notificationTemplate, notificationSendRequest.getVariableMap(),
                    notificationSendRequest.getTargets().stream().map(User::getFcmToken).toList());

            List<Notification> sendList = notificationSendRequest.getTargets().stream()
                    .map((user) -> Notification.of(user, notificationTemplate, true)).toList();

            notificationRepository.saveAll(sendList);
        } catch (FirebaseMessagingException fe) {
            log.error(fe.getMessage(), fe);

            List<Notification> failList = notificationSendRequest.getTargets().stream()
                    .map((user) -> Notification.failOf(user, fe.getMessage(),
                            notificationTemplate.getTemplateCode())).toList();
            notificationRepository.saveAll(failList);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public NotificationListResponse getNotificationList(Long userId, Pageable pageable) {
        User user = userRepository.findById(userId).orElseThrow(() -> new DataNotFoundException(WwoossResponseCode.NOT_FOUND_DATA, "존재하지 않는 유저입니다."));
        Slice<Notification> findNotificationList = notificationRepository.findByUserAndIsShow(user, true, pageable);
        List<NotificationResponse> notificationResponses = findNotificationList.getContent().stream().map(NotificationResponse::from).toList();

        return NotificationListResponse.of(findNotificationList.hasNext(), notificationResponses);
    }
}
