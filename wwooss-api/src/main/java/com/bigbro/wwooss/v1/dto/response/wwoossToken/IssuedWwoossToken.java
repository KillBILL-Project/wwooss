package com.bigbro.wwooss.v1.dto.response.wwoossToken;

import com.bigbro.wwooss.v1.entity.token.WwoossToken;
import com.bigbro.wwooss.v1.enumType.IssueType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
@AllArgsConstructor
public class IssuedWwoossToken {
    private Long wwoossTokenId;
    private IssueType issueType;
    private Long value;

    public static IssuedWwoossToken from(WwoossToken wwoossToken) {
        return IssuedWwoossToken.builder()
                .wwoossTokenId(wwoossToken.getWwoossTokenId())
                .issueType(wwoossToken.getIssueType())
                .value(wwoossToken.getValue())
                .build();
    }

}
