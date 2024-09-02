package com.bigbro.wwooss.v1.service.wwoossToken;

import com.bigbro.wwooss.v1.dto.response.wwoossToken.IssuedWwoossToken;
import com.bigbro.wwooss.v1.enumType.IssueType;

public interface WwoossTokenService {

    /**
     * 토큰 발급
     * @param userId
     * @param issueType
     * @return
     */
    IssuedWwoossToken issueWwoossToken(Long userId, IssueType issueType);
}
