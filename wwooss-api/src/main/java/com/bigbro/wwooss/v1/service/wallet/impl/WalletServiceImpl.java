package com.bigbro.wwooss.v1.service.wallet.impl;

import com.bigbro.wwooss.v1.entity.token.WwoossToken;
import com.bigbro.wwooss.v1.entity.user.User;
import com.bigbro.wwooss.v1.entity.wallet.Wallet;
import com.bigbro.wwooss.v1.enumType.IssueType;
import com.bigbro.wwooss.v1.exception.DataNotFoundException;
import com.bigbro.wwooss.v1.repository.user.UserRepository;
import com.bigbro.wwooss.v1.repository.wallet.WalletRepository;
import com.bigbro.wwooss.v1.repository.wwoossToken.WwoossTokenRepository;
import com.bigbro.wwooss.v1.response.WwoossResponseCode;
import com.bigbro.wwooss.v1.service.wallet.WalletService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class WalletServiceImpl implements WalletService {

    private final WalletRepository walletRepository;

    private final WwoossTokenRepository wwoossTokenRepository;

    private final UserRepository userRepository;

    @Override
    @Transactional
    public void createWallet(User user) {
        walletRepository.save(Wallet.from(user));
    }

    @Override
    @Transactional(readOnly = true)
    public Long getTotalTokenValueByType(Long userId, IssueType issueType) {
        User user = userRepository.findById(userId).orElseThrow(() -> new DataNotFoundException(WwoossResponseCode.NOT_FOUND_DATA, "유저를 찾을 수 없습니다."));
        Wallet wallet = walletRepository.findByUser(user).orElseThrow(() -> new DataNotFoundException(WwoossResponseCode.NOT_FOUND_DATA, "유저를 찾을 수 없습니다."));
        List<WwoossToken> wwoossTokens = Objects.isNull(issueType) ? wwoossTokenRepository.findWwoossTokenByWallet(wallet)
                : wwoossTokenRepository.findWwoossTokenByWalletAndIssueType(wallet, issueType);

        return wwoossTokens.stream()
                .mapToLong(WwoossToken::getValue)
                .sum();
    }
}
