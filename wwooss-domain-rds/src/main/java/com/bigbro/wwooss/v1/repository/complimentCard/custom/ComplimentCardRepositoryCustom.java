package com.bigbro.wwooss.v1.repository.complimentCard.custom;

import com.bigbro.wwooss.v1.entity.complimentCard.ComplimentCard;
import com.bigbro.wwooss.v1.entity.user.User;
import com.bigbro.wwooss.v1.enumType.CardType;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

public interface ComplimentCardRepositoryCustom {

    Slice<ComplimentCard> findByUserAndShowYAndCardType(User user, boolean show, CardType cardType, Pageable pageable);
}
