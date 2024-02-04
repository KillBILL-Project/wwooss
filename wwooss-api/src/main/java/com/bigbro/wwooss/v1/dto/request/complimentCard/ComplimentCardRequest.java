package com.bigbro.wwooss.v1.dto.request.complimentCard;

import com.bigbro.wwooss.v1.enumType.ComplimentType;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor
@Getter
@Builder
public class ComplimentCardRequest {

    private Long userId;

    private ComplimentType complimentType;

    public static ComplimentCardRequest of(long userId, ComplimentType complimentType) {
        return ComplimentCardRequest.builder()
                .userId(userId)
                .complimentType(complimentType)
                .build();
    }

}
