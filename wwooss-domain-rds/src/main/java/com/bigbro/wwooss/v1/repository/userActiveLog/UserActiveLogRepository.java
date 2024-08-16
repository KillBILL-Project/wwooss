package com.bigbro.wwooss.v1.repository.userActiveLog;

import com.bigbro.wwooss.v1.entity.userActiveLog.UserActiveLog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserActiveLogRepository extends JpaRepository<UserActiveLog, Long> {
}
