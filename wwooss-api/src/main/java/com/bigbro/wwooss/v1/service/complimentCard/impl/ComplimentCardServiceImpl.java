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


    @Override
    @Transactional
    public void createComplimentCard(ComplimentCardRequest complimentCardRequest) {
        User user =
                userRepository.findById(complimentCardRequest.getUserId()).orElseThrow(() -> new DataNotFoundException(WwoossResponseCode.NOT_FOUND_DATA, "존재하지 않는 유저입니다."));
        List<ComplimentConditionLog> complimentConditionLogs = complimentConditionLogRepository.findByUserAndComplimentTypeOrderByCreatedAtDesc(user, complimentCardRequest.getComplimentType());

        // 통합 칭찬 스티커 생성
        createIntegrateComplimentCard(user, complimentConditionLogs);

        // 주간 칭찬 스티커 생성
        createWeeklyComplimentCard(user, complimentConditionLogs.get(0));
    }

    private void createIntegrateComplimentCard(User user ,List<ComplimentConditionLog> complimentConditionLogs) {
        CardCode cardCode;
        // 통합 칭찬 스티커 생성
        if (complimentConditionLogs.size() >= 30 && complimentConditionLogs.size() < 100) {
            cardCode = CardCode.login_30;
        } else if (complimentConditionLogs.size() >= 100 && complimentConditionLogs.size() < 365) {
            cardCode = CardCode.login_100;
        } else if(complimentConditionLogs.size() >= 365) {
            cardCode = CardCode.login_365;
        } else {
            return;
        }

        ComplimentCardMeta complimentCardMeta = complimentCardMetaRepository.findByCardCode(
                cardCode).orElseThrow(() -> new DataNotFoundException(WwoossResponseCode.NOT_FOUND_DATA, "존재하지 칭찬 코드입니다."));
        ComplimentCard complimentCard = complimentCardRepository.findByUserAndExpireAndComplimentCardMeta(user, false, complimentCardMeta);
        if (Objects.nonNull(complimentCard)) return;

        complimentCardRepository.save(ComplimentCard.of(user, complimentCardMeta));
    }

    private void createWeeklyComplimentCard(User user, ComplimentConditionLog latestComplimentLog) {
        if (latestComplimentLog.getContinuity() != 3) return;

        // 연속 3회
        CardCode cardCode = CardCode.login_03_in_a_low;
        ComplimentCardMeta complimentCardMeta = complimentCardMetaRepository.findByCardCode(cardCode)
                .orElseThrow(() -> new DataNotFoundException(WwoossResponseCode.NOT_FOUND_DATA, "존재하지 칭찬 코드입니다."));
        ComplimentCard complimentCard = complimentCardRepository.findByUserAndExpireAndComplimentCardMeta(user, false, complimentCardMeta);
        if (Objects.nonNull(complimentCard)) return;

        complimentCardRepository.save(ComplimentCard.of(user, complimentCardMeta));
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
