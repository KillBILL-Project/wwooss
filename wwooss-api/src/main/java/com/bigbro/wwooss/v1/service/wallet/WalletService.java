package com.bigbro.wwooss.v1.service.wallet;

import com.bigbro.wwooss.v1.entity.user.User;
import com.bigbro.wwooss.v1.enumType.IssueType;

public interface WalletService {
    void createWallet(User user);

    /**
     * token type 별 총합
     * @param userId
     * @param issueType
     * @return
     */
    Double getTotalTokenValueByType(Long userId, IssueType issueType);
}
