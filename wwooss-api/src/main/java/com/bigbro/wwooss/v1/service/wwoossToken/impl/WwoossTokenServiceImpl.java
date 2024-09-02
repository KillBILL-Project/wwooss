package com.bigbro.wwooss.v1.service.wwoossToken.impl;

import com.bigbro.wwooss.v1.dto.response.wwoossToken.IssuedWwoossToken;
import com.bigbro.wwooss.v1.entity.token.WwoossToken;
import com.bigbro.wwooss.v1.entity.user.User;
import com.bigbro.wwooss.v1.entity.wallet.Wallet;
import com.bigbro.wwooss.v1.enumType.IssueType;
import com.bigbro.wwooss.v1.exception.DataNotFoundException;
import com.bigbro.wwooss.v1.repository.quizHistory.QuizHistoryRepository;
import com.bigbro.wwooss.v1.repository.user.UserRepository;
import com.bigbro.wwooss.v1.repository.wallet.WalletRepository;
import com.bigbro.wwooss.v1.repository.wwoossToken.WwoossTokenRepository;
import com.bigbro.wwooss.v1.response.WwoossResponseCode;
import com.bigbro.wwooss.v1.service.wwoossToken.WwoossTokenService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class WwoossTokenServiceImpl implements WwoossTokenService {

    private final WalletRepository walletRepository;

    private final UserRepository userRepository;

    private final WwoossTokenRepository wwoossTokenRepository;

    @Override
    @Transactional
    public IssuedWwoossToken issueWwoossToken(Long userId, IssueType issueType) {
        User user = userRepository.findById(userId).orElseThrow(() -> new DataNotFoundException(WwoossResponseCode.NOT_FOUND_DATA, "유저를 찾을 수 없습니다."));
        Wallet wallet = walletRepository.findByUser(user).orElseThrow(() -> new DataNotFoundException(WwoossResponseCode.NOT_FOUND_DATA, "유저를 찾을 수 없습니다."));

        WwoossToken wwoossToken = WwoossToken.of(issueType, wallet);
        WwoossToken savedToken = wwoossTokenRepository.save(wwoossToken);
        return IssuedWwoossToken.from(savedToken);
    }

}
