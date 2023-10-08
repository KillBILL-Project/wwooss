package com.bigbro.wwooss.v1.repository.trash.can;

import com.bigbro.wwooss.v1.domain.entity.trash.can.TrashCanContents;
import com.bigbro.wwooss.v1.domain.entity.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TrashCanContentsRepository extends JpaRepository<TrashCanContents, Long> {

    List<TrashCanContents> findAllByUser(User user);
}
