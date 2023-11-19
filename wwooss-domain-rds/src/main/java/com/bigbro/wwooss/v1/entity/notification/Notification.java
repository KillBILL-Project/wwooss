package com.bigbro.wwooss.v1.entity.notification;

import com.bigbro.wwooss.v1.entity.base.BaseEntity;
import com.bigbro.wwooss.v1.entity.user.User;
import com.bigbro.wwooss.v1.enumType.NotificationTemplateCode;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.Comment;

@Entity
@Table(name = "notification")
@SuperBuilder
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Notification extends BaseEntity {

    @Id
    @Column(name = "notification_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long notificationId;

    @Comment("템플릿 코드")
    @Column(name = "template_code")
    @Enumerated(value = EnumType.STRING)
    private NotificationTemplateCode templateCode;

    @Comment("푸쉬 알림 제목")
    @Column(name = "title")
    private String title;

    @Comment("푸쉬 알림 내용")
    @Column(name = "message")
    private String message;

    @Comment("배치성 알림 여부. 예) 공지")
    @Column(name = "batch")
    private boolean batch;

    @Comment("알림함에 노출 여부")
    @Column(name = "show")
    private boolean show;

    @Comment("딥링크")
    @Column(name = "deep_link")
    private String deepLink;

    @Comment("발송 성공 여부")
    @Column(name = "status")
    private boolean status;

    @Comment("실패 매세지")
    @Column(name = "fail_message")
    private String failMessage;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    public static Notification failOf(User user, String failMessage, NotificationTemplateCode templateCode) {
        return Notification.builder()
                .user(user)
                .templateCode(templateCode)
                .failMessage(failMessage)
                .build();
    }

    public static Notification Of(User user, NotificationTemplate notificationTemplate, boolean status) {
        return Notification.builder()
                .user(user)
                .templateCode(notificationTemplate.getTemplateCode())
                .title(notificationTemplate.getTitle())
                .message(notificationTemplate.getMessage())
                .batch(notificationTemplate.isBatch())
                .show(notificationTemplate.isShow())
                .deepLink(notificationTemplate.getDeepLink())
                .status(status)
                .build();
    }

}
