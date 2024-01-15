package com.bigbro.wwooss.v1.service.complimentCard;

import com.bigbro.wwooss.v1.dto.request.complimentCard.ComplimentCardRequest;

public interface ComplimentCardService {

    /**
     * 칭찬 카드 생성
     */
    void createComplimentCard(ComplimentCardRequest complimentCardRequest);
}
