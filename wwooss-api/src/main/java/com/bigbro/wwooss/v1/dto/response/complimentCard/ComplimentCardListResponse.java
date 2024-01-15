package com.bigbro.wwooss.v1.dto.response.complimentCard;

import java.util.List;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class ComplimentCardListResponse {

    boolean hasNext;

    List<ComplimentCardResponse> complimentCardResponses;

    public static ComplimentCardListResponse of(boolean hasNext, List<ComplimentCardResponse> complimentCardResponses) {
        return ComplimentCardListResponse.builder()
                .hasNext(hasNext)
                .complimentCardResponses(complimentCardResponses)
                .build();
    }
}
