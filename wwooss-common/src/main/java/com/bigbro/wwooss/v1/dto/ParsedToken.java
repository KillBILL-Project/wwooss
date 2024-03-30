package com.bigbro.wwooss.v1.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class ParsedToken {
    private String email;
    private String socialId;

    public static ParsedToken of(String email, String socialId) {
        return ParsedToken.builder()
                .email(email)
                .socialId(socialId)
                .build();
    }
}
