package com.bigbro.wwooss.v1.repository.complimentCard;

import com.bigbro.wwooss.v1.entity.complimentCard.ComplimentConditionLog;
import com.bigbro.wwooss.v1.entity.user.User;
import com.bigbro.wwooss.v1.enumType.ComplimentType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ComplimentConditionLogRepository extends JpaRepository<ComplimentConditionLog, Long> {

    List<ComplimentConditionLog> findByUserAndComplimentTypeOrderByCreatedAtDesc(User user, ComplimentType complimentType);


    List<ComplimentConditionLog> findByUser(User user);

    void deleteByUser(User user);

}
