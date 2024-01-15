package com.bigbro.wwooss.v1.service.complimentCard;

import com.bigbro.wwooss.v1.dto.request.complimentCard.ComplimentCardRequest;
import com.bigbro.wwooss.v1.dto.response.complimentCard.ComplimentCardListResponse;
import com.bigbro.wwooss.v1.enumType.CardType;
import org.springframework.data.domain.Pageable;

public interface ComplimentCardService {

    /**
     * 칭찬 카드 생성
     */
    void createComplimentCard(ComplimentCardRequest complimentCardRequest);

    /**
     * 칭찬 카드 조회
     * @param userId
     * @param cardType 칭찬 카드 종류 [주간 - WEEKLY / 통합 - INTEGRATE]
     */
    ComplimentCardListResponse getComplimentCardList(Long userId, CardType cardType, Pageable pageable);
}
