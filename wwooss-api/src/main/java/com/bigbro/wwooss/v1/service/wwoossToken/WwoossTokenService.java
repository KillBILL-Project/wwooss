package com.bigbro.wwooss.v1.service.wwoossToken;

import com.bigbro.wwooss.v1.enumType.IssueType;

public interface WwoossTokenService {

    void issueWwoossToken(Long userId, IssueType issueType);
}
