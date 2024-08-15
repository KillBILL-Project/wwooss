package com.bigbro.wwooss.v1.dto.request.wwoossToken;

import com.bigbro.wwooss.v1.enumType.IssueType;
import lombok.*;

import javax.validation.constraints.NotNull;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class IssueTokenRequest {

    @NotNull(message = "issue type은 필수값입니다.")
    private IssueType issueType;
}
