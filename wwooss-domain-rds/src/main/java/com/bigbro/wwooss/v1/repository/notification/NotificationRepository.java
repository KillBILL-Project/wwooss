package com.bigbro.wwooss.v1.repository.notification;

import com.bigbro.wwooss.v1.entity.notification.Notification;
import com.bigbro.wwooss.v1.entity.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification, Long> {

    void deleteByUser(User user);

    List<Notification> findByUser(User user);

}
