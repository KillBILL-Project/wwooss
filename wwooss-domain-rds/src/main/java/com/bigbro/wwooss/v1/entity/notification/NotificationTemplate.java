package com.bigbro.wwooss.v1.entity.notification;

import com.bigbro.wwooss.v1.entity.base.BaseEntity;
import com.bigbro.wwooss.v1.enumType.NotificationTemplateCode;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.Comment;

@Entity
@Table(name = "notification_template")
@SuperBuilder
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class NotificationTemplate extends BaseEntity {

    @Id
    @Column(name = "notification_template_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long notificationTemplateId;

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

    @Comment("템플릿 코드")
    @Column(name = "template_code")
    @Enumerated(value = EnumType.STRING)
    private NotificationTemplateCode templateCode;

    @Comment("딥링크")
    @Column(name = "deep_link")
    private String deepLink;

}
