package com.bigbro.wwooss.v1.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ComplimentCardIcon {

    private Long complimentCardId;

    private String cardImage;

    public static ComplimentCardIcon of(Long complimentCardId, String cardImage) {
        return ComplimentCardIcon.builder()
                .complimentCardId(complimentCardId)
                .cardImage(cardImage)
                .build();
    }
}
