package com.bigbro.wwooss.v1.repository.userActiveLog;

import com.bigbro.wwooss.v1.entity.user.User;
import com.bigbro.wwooss.v1.entity.userActiveLog.UserActiveLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UserActiveLogRepository extends JpaRepository<UserActiveLog, Long> {

    @Query("SELECT SUM(log.value) FROM UserActiveLog log WHERE log.user = :user")
    Double selectSumActiveLog(@Param("user") User user);

    @Query("SELECT SUM(log.value) FROM UserActiveLog log WHERE log.verified = true AND log.user = :user")
    Double selectSumVerifiedLog(@Param("user") User user);
}
