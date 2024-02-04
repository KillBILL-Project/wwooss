package com.bigbro.wwooss.v1.repository.complimentCard.custom;

import com.bigbro.wwooss.v1.entity.complimentCard.ComplimentCard;
import com.bigbro.wwooss.v1.entity.complimentCard.ComplimentCardMeta;
import com.bigbro.wwooss.v1.entity.user.User;
import com.bigbro.wwooss.v1.enumType.CardType;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

public interface ComplimentCardRepositoryCustom {

    Slice<ComplimentCard> findByUserAndShowYAndCardType(User user, boolean expire, CardType cardType,
            Pageable pageable);

    List<ComplimentCard> findByUserBetweenDate(User user, LocalDateTime fromDate, LocalDateTime toDate);
}
