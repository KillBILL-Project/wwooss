package com.bigbro.wwooss.v1.repository.notification;

import com.bigbro.wwooss.v1.entity.notification.Notification;
import com.bigbro.wwooss.v1.entity.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NotificationRepository extends JpaRepository<Notification, Long> {

    void deleteByUser(User user);

}
