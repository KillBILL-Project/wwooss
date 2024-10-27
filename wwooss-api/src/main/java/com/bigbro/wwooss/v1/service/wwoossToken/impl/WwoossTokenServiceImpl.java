package com.bigbro.wwooss.v1.service.wwoossToken.impl;

import com.bigbro.wwooss.v1.dto.response.wwoossToken.IssuedWwoossToken;
import com.bigbro.wwooss.v1.entity.quiz.Quiz;
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
import com.bigbro.wwooss.v1.service.template.RedisTemplateService;
import com.bigbro.wwooss.v1.service.wwoossToken.WwoossTokenService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;


@Service
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class WwoossTokenServiceImpl implements WwoossTokenService {

    private final WalletRepository walletRepository;

    private final UserRepository userRepository;

    private final WwoossTokenRepository wwoossTokenRepository;

    private final RedisTemplateService redisTemplateService;

    @Override
    @Transactional
    public IssuedWwoossToken issueWwoossToken(Long userId, IssueType issueType) {
        if(!checkDailyMaxValue(issueType, userId)) {
            return null;
        }
        User user = userRepository.findById(userId).orElseThrow(() -> new DataNotFoundException(WwoossResponseCode.NOT_FOUND_DATA, "유저를 찾을 수 없습니다."));
        Wallet wallet = walletRepository.findByUser(user).orElseThrow(() -> new DataNotFoundException(WwoossResponseCode.NOT_FOUND_DATA, "유저를 찾을 수 없습니다."));

        WwoossToken wwoossToken = WwoossToken.of(issueType, wallet);
        WwoossToken savedToken = wwoossTokenRepository.save(wwoossToken);
        return IssuedWwoossToken.from(savedToken);
    }

    /**
     * 하루 최대치 체크
     */
    private boolean checkDailyMaxValue(IssueType issueType, Long userId) {
        String redisKey = getRedisKey(issueType, userId);
        if(Objects.isNull(redisKey)) {
            return true;
        }

        String value = redisTemplateService.getRedisByKey(redisKey);
        return Objects.nonNull(value);
    }

    private String getRedisKey(IssueType issueType, Long userId) {
        switch (issueType) {
            case QUIZ -> {
                return IssueType.QUIZ.name() + userId.toString();
            }
            case CLEAN_TRASH -> {
                return IssueType.CLEAN_TRASH.name() + userId.toString();
            }
            case CHECK_REPORT -> {
                return IssueType.CHECK_REPORT.name() + userId.toString();
            }
            case RECORD_RECYCLE -> {
                return IssueType.RECORD_RECYCLE.name() + userId.toString();
            }
            default -> {
                return null;
            }
        }
    }
}
