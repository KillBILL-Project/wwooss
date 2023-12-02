package com.bigbro.wwooss.v1.repository.notification;

import com.bigbro.wwooss.v1.entity.notification.NotificationTemplate;
import com.bigbro.wwooss.v1.enumType.NotificationTemplateCode;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NotificationTemplateRepository extends JpaRepository<NotificationTemplate, Long> {
    NotificationTemplate findByTemplateCode(NotificationTemplateCode templateCode);
}
