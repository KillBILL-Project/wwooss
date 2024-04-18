package com.bigbro.wwooss.v1.service.notification.impl;

import com.bigbro.wwooss.v1.dto.request.notification.NotificationSendRequest;
import com.bigbro.wwooss.v1.dto.response.notification.NotificationByDate;
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

import java.time.format.DateTimeFormatter;
import java.util.*;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.bigbro.wwooss.v1.utils.DateUtil.YEAR_MONTH_DAY_FORMAT;

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

        Map<String, ArrayList<NotificationResponse>> notificationByDate = new HashMap<>();
        findNotificationList.getContent().forEach((notification) -> {
            String formattedDate = notification.getCreatedAt().format(DateTimeFormatter.ofPattern(YEAR_MONTH_DAY_FORMAT));
            ArrayList<NotificationResponse> notificationResponse = notificationByDate.get(formattedDate);
            NotificationResponse response = NotificationResponse.from(notification);

            if(Objects.isNull(notificationResponse)) {
                ArrayList<NotificationResponse> init = new ArrayList<>();
                init.add(response);
                notificationByDate.put(formattedDate, init);
            } else {
                notificationResponse.add(response);
                notificationByDate.put(formattedDate, notificationResponse);
            }

        });

        List<NotificationByDate> responses = new ArrayList<>();
        notificationByDate.forEach((key, value) -> {
            responses.add(NotificationByDate.of(key, value));
        });

        return NotificationListResponse.of(findNotificationList.hasNext(), responses);
    }
}
