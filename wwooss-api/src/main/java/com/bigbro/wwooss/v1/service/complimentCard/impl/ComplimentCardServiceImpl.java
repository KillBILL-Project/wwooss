package com.bigbro.wwooss.v1.service.complimentCard.impl;

import com.bigbro.wwooss.v1.dto.request.complimentCard.ComplimentCardRequest;
import com.bigbro.wwooss.v1.dto.response.complimentCard.ComplimentCardListResponse;
import com.bigbro.wwooss.v1.dto.response.complimentCard.ComplimentCardResponse;
import com.bigbro.wwooss.v1.entity.complimentCard.ComplimentCard;
import com.bigbro.wwooss.v1.entity.complimentCard.ComplimentCardMeta;
import com.bigbro.wwooss.v1.entity.complimentCard.ComplimentConditionLog;
import com.bigbro.wwooss.v1.entity.user.User;
import com.bigbro.wwooss.v1.enumType.CardCode;
import com.bigbro.wwooss.v1.enumType.CardType;
import com.bigbro.wwooss.v1.enumType.ComplimentType;
import com.bigbro.wwooss.v1.exception.DataNotFoundException;
import com.bigbro.wwooss.v1.repository.complimentCard.ComplimentCardMetaRepository;
import com.bigbro.wwooss.v1.repository.complimentCard.ComplimentCardRepository;
import com.bigbro.wwooss.v1.repository.complimentCard.ComplimentConditionLogRepository;
import com.bigbro.wwooss.v1.repository.user.UserRepository;
import com.bigbro.wwooss.v1.response.WwoossResponseCode;
import com.bigbro.wwooss.v1.service.complimentCard.ComplimentCardService;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Optional;

import com.bigbro.wwooss.v1.utils.ImageUtil;
import org.springframework.data.domain.Pageable;
import java.util.List;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class ComplimentCardServiceImpl implements ComplimentCardService {

    private final ComplimentCardRepository complimentCardRepository;

    private final ComplimentCardMetaRepository complimentCardMetaRepository;

    private final ComplimentConditionLogRepository complimentConditionLogRepository;

    private final UserRepository userRepository;

    private final ImageUtil imageUtil;

    private static final int LOG_IN_COUNT_30 = 30;
    private static final int LOG_IN_COUNT_100 = 100;
    private static final int LOG_IN_COUNT_365 = 365;

    private static final int CONTINUITY_LOG_IN_COUNT = 3;
    private static final int CONTINUITY_THROW_TRASH_COUNT = 3;


    @Override
    @Transactional
    public void createComplimentCard(ComplimentCardRequest complimentCardRequest) {
        User user =
                userRepository.findById(complimentCardRequest.getUserId()).orElseThrow(() -> new DataNotFoundException(WwoossResponseCode.NOT_FOUND_DATA, "존재하지 않는 유저입니다."));
        List<ComplimentConditionLog> complimentConditionLogs = complimentConditionLogRepository.findByUserAndComplimentTypeOrderByCreatedAtDesc(user, complimentCardRequest.getComplimentType());

        // 통합 칭찬 스티커 생성
        CardCode cardCode = getCardCode(complimentConditionLogs, complimentCardRequest.getComplimentType());
        if(Objects.nonNull(cardCode) ) {
            createIntegrateComplimentCard(user, cardCode);
        }

        // 주간 칭찬 스티커 생성
        createWeeklyComplimentCard(user, complimentConditionLogs.get(0), getWeeklyComplimentCondition(complimentCardRequest.getComplimentType()));
    }

    private void createIntegrateComplimentCard(User user , CardCode cardCode) {
        ComplimentCardMeta complimentCardMeta = complimentCardMetaRepository.findByCardCode(
                cardCode).orElseThrow(() -> new DataNotFoundException(WwoossResponseCode.NOT_FOUND_DATA, "존재하지 칭찬 코드입니다."));
        ComplimentCard complimentCard = complimentCardRepository.findByUserAndExpireAndComplimentCardMeta(user, false, complimentCardMeta);
        if (Objects.nonNull(complimentCard)) return;

        complimentCardRepository.save(ComplimentCard.of(user, complimentCardMeta));
    }

    /**
     *  통합 미션 추가 시 각 미션에 맞게 cardcode 추가
     */
    private CardCode getCardCode(List<ComplimentConditionLog> complimentConditionLogs, ComplimentType complimentType) {
        CardCode cardCode = null;
        switch (complimentType) {
            case LOGIN -> cardCode = getLoginCardCode(complimentConditionLogs);
        };
        return cardCode;
    }

    private CardCode getLoginCardCode(List<ComplimentConditionLog> complimentConditionLogs) {
        CardCode cardCode;
        // 통합 칭찬 스티커 생성
        if (complimentConditionLogs.size() >= LOG_IN_COUNT_30 && complimentConditionLogs.size() < LOG_IN_COUNT_100) {
            cardCode = CardCode.login_30;
        } else if (complimentConditionLogs.size() >= LOG_IN_COUNT_100 && complimentConditionLogs.size() < LOG_IN_COUNT_365) {
            cardCode = CardCode.login_100;
        } else if(complimentConditionLogs.size() >= LOG_IN_COUNT_365) {
            cardCode = CardCode.login_365;
        } else {
            return null;
        }
        return cardCode;
    }

    private void createWeeklyComplimentCard(User user, ComplimentConditionLog latestComplimentLog, int continuityCondition) {
        if (latestComplimentLog.getContinuity() != continuityCondition) return;

        // 연속 3회
        CardCode cardCode = CardCode.login_03_in_a_low;
        ComplimentCardMeta complimentCardMeta = complimentCardMetaRepository.findByCardCode(cardCode)
                .orElseThrow(() -> new DataNotFoundException(WwoossResponseCode.NOT_FOUND_DATA, "존재하지 칭찬 코드입니다."));
        ComplimentCard complimentCard = complimentCardRepository.findByUserAndExpireAndComplimentCardMeta(user, false, complimentCardMeta);
        if (Objects.nonNull(complimentCard)) return;

        complimentCardRepository.save(ComplimentCard.of(user, complimentCardMeta));
    }

    private int getWeeklyComplimentCondition(ComplimentType complimentType) {
        int count = 0;
        switch (complimentType) {
            case LOGIN -> count = CONTINUITY_LOG_IN_COUNT;
            case THROW_TRASH -> count = CONTINUITY_THROW_TRASH_COUNT;
        }
        return count;
    }

    @Override
    @Transactional(readOnly = true)
    public ComplimentCardListResponse getComplimentCardList(Long userId, CardType cardType, Pageable pageable) {
        User user =
                userRepository.findById(userId).orElseThrow(() -> new DataNotFoundException(WwoossResponseCode.NOT_FOUND_DATA, "존재하지 않는 유저입니다."));
        Slice<ComplimentCard> complimentCardList = complimentCardRepository.findByUserAndShowYAndCardType(user, false,
                cardType, pageable);
        List<ComplimentCardResponse> cardResponseList =
                complimentCardList.getContent().stream().map((card) -> ComplimentCardResponse.of(card.getComplimentCardId(), card.getComplimentCardMeta(),
                        imageUtil.baseComplimentCardImagePath())).toList();

        return ComplimentCardListResponse.of(complimentCardList.hasNext(), cardResponseList);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ComplimentCardResponse> getComplimentCardAtDate(User user, LocalDateTime fromDate,
            LocalDateTime toDate) {
        List<ComplimentCard> complimentCardList = complimentCardRepository.findByUserBetweenDate(user, fromDate, toDate);
        return complimentCardList.stream().map((card) -> ComplimentCardResponse.of(card.getComplimentCardId(), card.getComplimentCardMeta(),
                                        imageUtil.baseComplimentCardImagePath())).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public ComplimentCardResponse getComplimentCardDetail(Long complimentCardId) {
        ComplimentCard complimentCard =
                complimentCardRepository.findById(complimentCardId)
                        .orElseThrow(() -> new DataNotFoundException(WwoossResponseCode.NOT_FOUND_DATA, "존재하지 칭찬카드"));
        return ComplimentCardResponse.of(complimentCard.getComplimentCardId(), complimentCard.getComplimentCardMeta()
                , imageUtil.baseComplimentCardImagePath());
    }

}
