package com.bigbro.wwooss.v1.repository.complimentCard.custom;

import static com.bigbro.wwooss.v1.entity.complimentCard.QComplimentCard.complimentCard;

import com.bigbro.wwooss.v1.entity.complimentCard.ComplimentCard;
import com.bigbro.wwooss.v1.entity.user.User;
import com.bigbro.wwooss.v1.enumType.CardType;
import com.bigbro.wwooss.v1.utils.PagingUtil;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class ComplimentCardRepositoryImpl implements ComplimentCardRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public Slice<ComplimentCard> findByUserAndShowYAndCardType(User user, boolean show, CardType cardType,
            Pageable pageable) {
        JPAQuery<ComplimentCard> complimentCardJPAQuery = jpaQueryFactory.selectFrom(complimentCard)
                .where(complimentCard.user.eq(user)
                        .and(complimentCard.show.eq(show))
                        .and(complimentCard.complimentCardMeta.cardType.eq(cardType)));
        return PagingUtil.getSlice(complimentCardJPAQuery, pageable);
    }
}
