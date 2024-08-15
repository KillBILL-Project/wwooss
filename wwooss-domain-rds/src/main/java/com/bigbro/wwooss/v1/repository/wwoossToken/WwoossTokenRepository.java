package com.bigbro.wwooss.v1.repository.wwoossToken;

import com.bigbro.wwooss.v1.entity.token.WwoossToken;
import com.bigbro.wwooss.v1.entity.wallet.Wallet;
import com.bigbro.wwooss.v1.enumType.IssueType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface WwoossTokenRepository extends JpaRepository<WwoossToken, Long> {

    List<WwoossToken> findWwoossTokenByWalletAndIssueType(Wallet wallet, IssueType issueType);
    List<WwoossToken> findWwoossTokenByWallet(Wallet wallet);
}
