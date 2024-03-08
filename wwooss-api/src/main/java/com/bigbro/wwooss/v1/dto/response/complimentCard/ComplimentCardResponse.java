package com.bigbro.wwooss.v1.dto.response.complimentCard;

import com.bigbro.wwooss.v1.entity.complimentCard.ComplimentCard;
import com.bigbro.wwooss.v1.entity.complimentCard.ComplimentCardMeta;
import com.bigbro.wwooss.v1.enumType.CardType;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class ComplimentCardResponse {

    Long complimentCardId;

    String title;

    String contents;

    CardType cardType;

    String cardImage;

    public static ComplimentCardResponse of(long complimentCardId, ComplimentCardMeta complimentCardMeta, String imageBase) {
        return ComplimentCardResponse.builder()
                .complimentCardId(complimentCardId)
                .title(complimentCardMeta.getTitle())
                .contents(complimentCardMeta.getContents())
                .cardType(complimentCardMeta.getCardType())
                .cardImage(imageBase + complimentCardMeta.getCardImage())
                .build();
    }

}
