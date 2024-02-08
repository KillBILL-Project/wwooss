package com.bigbro.wwooss.v1.repository.complimentCard;

import com.bigbro.wwooss.v1.entity.complimentCard.ComplimentCard;
import com.bigbro.wwooss.v1.entity.complimentCard.ComplimentCardMeta;
import com.bigbro.wwooss.v1.entity.user.User;
import com.bigbro.wwooss.v1.repository.complimentCard.custom.ComplimentCardRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ComplimentCardRepository extends JpaRepository<ComplimentCard, Long>, ComplimentCardRepositoryCustom {
    ComplimentCard findByUserAndExpireAndComplimentCardMeta(User user, boolean expire, ComplimentCardMeta complimentCardMeta);

    List<ComplimentCard> findByUser(User user);

    void deleteByUser(User user);

}
