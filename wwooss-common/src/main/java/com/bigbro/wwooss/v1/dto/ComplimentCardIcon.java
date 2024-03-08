package com.bigbro.wwooss.v1.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ComplimentCardIcon {

    private Long complimentCardId;

    private String cardImage;

    private String title;


    public static ComplimentCardIcon of(Long complimentCardId, String cardImage, String title) {
        return ComplimentCardIcon.builder()
                .complimentCardId(complimentCardId)
                .cardImage(cardImage)
                .title(title)
                .build();
    }
}
