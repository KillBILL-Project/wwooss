package com.bigbro.wwooss.v1.service.complimentCard;

import com.bigbro.wwooss.v1.dto.request.complimentCard.ComplimentCardRequest;
import com.bigbro.wwooss.v1.dto.response.complimentCard.ComplimentCardListResponse;
import com.bigbro.wwooss.v1.dto.response.complimentCard.ComplimentCardResponse;
import com.bigbro.wwooss.v1.entity.user.User;
import com.bigbro.wwooss.v1.enumType.CardType;
import java.time.LocalDateTime;
import java.util.List;
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

    /**
     * 특정 기간 내 칭찬 카드 조회
     */
    List<ComplimentCardResponse> getComplimentCardAtDate(User user, LocalDateTime fromDate, LocalDateTime toDate);

    /**
     * 칭찬 카드 상세 조회
     */
    ComplimentCardResponse getComplimentCardDetail(Long complimentCardId);
}
