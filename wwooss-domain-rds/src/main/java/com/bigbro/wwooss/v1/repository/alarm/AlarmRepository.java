package com.bigbro.wwooss.v1.repository.alarm;

import com.bigbro.wwooss.v1.entity.alarm.Alarm;
import com.bigbro.wwooss.v1.entity.user.User;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AlarmRepository extends JpaRepository<Alarm, Long> {
    List<Alarm> findAlarmByUser(User user);
}
