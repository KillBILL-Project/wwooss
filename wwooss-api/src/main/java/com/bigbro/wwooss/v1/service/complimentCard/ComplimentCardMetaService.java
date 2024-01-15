package com.bigbro.wwooss.v1.service.complimentCard;

import com.bigbro.wwooss.v1.dto.request.complimentCard.ComplimentCardMetaRequest;

public interface ComplimentCardMetaService {

    /**
     * 칭찬 카드 Meta 생성
     */
    void createComplimentCardMeta(ComplimentCardMetaRequest complimentCardMetaRequest);

}
