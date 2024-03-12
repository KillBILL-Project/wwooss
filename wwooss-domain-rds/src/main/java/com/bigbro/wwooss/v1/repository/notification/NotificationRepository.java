package com.bigbro.wwooss.v1.repository.notification;

import com.bigbro.wwooss.v1.entity.notification.Notification;
import com.bigbro.wwooss.v1.entity.user.User;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.data.domain.Pageable;
import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification, Long> {

    void deleteByUser(User user);

    List<Notification> findByUser(User user);

    Slice<Notification> findByUserAndIsShow(User user, Boolean isShow, Pageable pageable);

}
