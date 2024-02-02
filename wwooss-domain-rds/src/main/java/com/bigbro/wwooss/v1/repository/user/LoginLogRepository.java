package com.bigbro.wwooss.v1.repository.user;

import com.bigbro.wwooss.v1.entity.user.LoginLog;
import com.bigbro.wwooss.v1.entity.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LoginLogRepository extends JpaRepository<LoginLog, Long> {
    LoginLog findByUserOrderByCreatedAtDesc(User user);
}
