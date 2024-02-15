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

    private static final int COUNT_30 = 30;
    private static final int COUNT_100 = 100;
    private static final int COUNT_365 = 365;

    private static final int CONTINUITY_LOG_IN_COUNT = 3;
    private static final int CONTINUITY_THROW_TRASH_COUNT = 3;
    private static final int CONTINUITY_VIEW_REPORT_COUNT = 1;
    private static final int CONTINUITY_VIEW_TRASH_CAN_HISTORY_COUNT = 1;


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
            case LOGIN -> cardCode = getLoginCardCodeIntegrate(complimentConditionLogs);
            case VIEW_WEEKLY_REPORT -> cardCode = getViewWeeklyReportCardCodeIntegrate(complimentConditionLogs);
            case CLEAN_TRASH_CAN -> cardCode = getViewTrashCanHistoryCardCodeIntegrate(complimentConditionLogs);
        };
        return cardCode;
    }

    private CardCode getLoginCardCodeIntegrate(List<ComplimentConditionLog> complimentConditionLogs) {
        CardCode cardCode;
        // 통합 칭찬 스티커 생성
        if (complimentConditionLogs.size() >= COUNT_30 && complimentConditionLogs.size() < COUNT_100) {
            cardCode = CardCode.login_30;
        } else if (complimentConditionLogs.size() >= COUNT_100 && complimentConditionLogs.size() < COUNT_365) {
            cardCode = CardCode.login_100;
        } else if(complimentConditionLogs.size() >= COUNT_365) {
            cardCode = CardCode.login_365;
        } else {
            return null;
        }
        return cardCode;
    }

    private CardCode getViewWeeklyReportCardCodeIntegrate(List<ComplimentConditionLog> complimentConditionLogs) {
        CardCode cardCode;
        // 통합 칭찬 스티커 생성
        if (complimentConditionLogs.size() >= COUNT_30 && complimentConditionLogs.size() < COUNT_100) {
            cardCode = CardCode.view_weekly_report_30;
        } else if (complimentConditionLogs.size() >= COUNT_100 && complimentConditionLogs.size() < COUNT_365) {
            cardCode = CardCode.view_weekly_report_100;
        } else if(complimentConditionLogs.size() >= COUNT_365) {
            cardCode = CardCode.view_weekly_report_365;
        } else {
            return null;
        }
        return cardCode;
    }

    private CardCode getViewTrashCanHistoryCardCodeIntegrate(List<ComplimentConditionLog> complimentConditionLogs) {
        CardCode cardCode;
        // 통합 칭찬 스티커 생성
        if (complimentConditionLogs.size() >= COUNT_30 && complimentConditionLogs.size() < COUNT_100) {
            cardCode = CardCode.clean_trash_can_30;
        } else if (complimentConditionLogs.size() >= COUNT_100 && complimentConditionLogs.size() < COUNT_365) {
            cardCode = CardCode.clean_trash_can_100;
        } else if(complimentConditionLogs.size() >= COUNT_365) {
            cardCode = CardCode.clean_trash_can_365;
        } else {
            return null;
        }
        return cardCode;
    }

    private CardCode getCardCodeWeekly(ComplimentType complimentType) {
        CardCode cardCode = null;
        switch (complimentType) {
            case LOGIN -> cardCode = CardCode.login_03_in_a_low;
            case THROW_TRASH -> cardCode = CardCode.throw_trash_03_week;
            case VIEW_WEEKLY_REPORT -> cardCode = CardCode.view_weekly_report_1_week;
            case CLEAN_TRASH_CAN -> cardCode = CardCode.clean_trash_can_1_week;
        }
        return cardCode;
    }

    private void createWeeklyComplimentCard(User user, ComplimentConditionLog latestComplimentLog, int continuityCondition) {
        CardCode cardCode = getCardCodeWeekly(latestComplimentLog.getComplimentType());
        if (latestComplimentLog.getContinuity() != continuityCondition || Objects.isNull(cardCode)) return;

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
            case VIEW_WEEKLY_REPORT -> count = CONTINUITY_VIEW_REPORT_COUNT;
            case CLEAN_TRASH_CAN -> count = CONTINUITY_VIEW_TRASH_CAN_HISTORY_COUNT;
        }
        return count;
    }

}
