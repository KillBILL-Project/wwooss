package com.bigbro.wwooss.v1.repository.notification;

import com.bigbro.wwooss.v1.entity.notification.NotificationTemplate;
import com.bigbro.wwooss.v1.enumType.NotificationTemplateCode;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NotificationTemplateRepository extends JpaRepository<NotificationTemplate, Long> {
    Optional<NotificationTemplate> findByTemplateCode(NotificationTemplateCode templateCode);
}
