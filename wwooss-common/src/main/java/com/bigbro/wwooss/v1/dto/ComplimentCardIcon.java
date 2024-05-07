package com.bigbro.wwooss.v1.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ComplimentCardIcon {

    private Long complimentCardId;

    private String cardImage;

    private String title;

    private String contents;


    public static ComplimentCardIcon of(Long complimentCardId, String cardImage, String title, String contents) {
        return ComplimentCardIcon.builder()
                .complimentCardId(complimentCardId)
                .cardImage(cardImage)
                .title(title)
                .contents(contents)
                .build();
    }
}
