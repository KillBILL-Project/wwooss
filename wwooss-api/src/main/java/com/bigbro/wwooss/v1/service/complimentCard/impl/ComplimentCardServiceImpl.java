package com.bigbro.wwooss.v1.service.complimentCard.impl;

import com.bigbro.wwooss.v1.dto.request.complimentCard.ComplimentCardRequest;
import com.bigbro.wwooss.v1.dto.response.complimentCard.ComplimentCardListResponse;
import com.bigbro.wwooss.v1.dto.response.complimentCard.ComplimentCardResponse;
import com.bigbro.wwooss.v1.entity.complimentCard.ComplimentCard;
import com.bigbro.wwooss.v1.entity.complimentCard.ComplimentCardMeta;
import com.bigbro.wwooss.v1.entity.user.User;
import com.bigbro.wwooss.v1.enumType.CardType;
import com.bigbro.wwooss.v1.exception.DataNotFoundException;
import com.bigbro.wwooss.v1.repository.complimentCard.ComplimentCardMetaRepository;
import com.bigbro.wwooss.v1.repository.complimentCard.ComplimentCardRepository;
import com.bigbro.wwooss.v1.repository.user.UserRepository;
import com.bigbro.wwooss.v1.response.WwoossResponseCode;
import com.bigbro.wwooss.v1.service.complimentCard.ComplimentCardService;

import java.time.LocalDateTime;
import java.util.Optional;
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

    @Value("${aws.cloud-front.path}")
    private String imageBase;

    private final String COMPLIMENT_CARD_PATH = "compliment_card";

    private final ComplimentCardRepository complimentCardRepository;

    private final ComplimentCardMetaRepository complimentCardMetaRepository;

    private final UserRepository userRepository;


    @Override
    @Transactional
    public void createComplimentCard(ComplimentCardRequest complimentCardRequest) {
        ComplimentCardMeta complimentCardMeta = complimentCardMetaRepository.findByCardCode(
                complimentCardRequest.getCardCode()).orElseThrow(() -> new DataNotFoundException(
                WwoossResponseCode.NOT_FOUND_DATA, "존재하지 칭찬 코드입니다."));
        User user =
                userRepository.findById(complimentCardRequest.getUserId()).orElseThrow(() -> new DataNotFoundException(WwoossResponseCode.NOT_FOUND_DATA, "존재하지 않는 유저입니다."));

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
                                imageBase + COMPLIMENT_CARD_PATH)).toList();

        return ComplimentCardListResponse.of(complimentCardList.hasNext(), cardResponseList);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ComplimentCardResponse> getComplimentCardAtDate(User user, LocalDateTime fromDate,
            LocalDateTime toDate) {
        List<ComplimentCard> complimentCardList = complimentCardRepository.findByUserBetweenDate(user, fromDate, toDate);
        return complimentCardList.stream().map((card) -> ComplimentCardResponse.of(card.getComplimentCardId(), card.getComplimentCardMeta(),
                                        imageBase + COMPLIMENT_CARD_PATH)).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public ComplimentCardResponse getComplimentCardDetail(Long complimentCardId) {
        ComplimentCard complimentCard =
                complimentCardRepository.findById(complimentCardId)
                        .orElseThrow(() -> new DataNotFoundException(WwoossResponseCode.NOT_FOUND_DATA, "존재하지 칭찬카드"));
        return ComplimentCardResponse.of(complimentCard.getComplimentCardId(), complimentCard.getComplimentCardMeta()
                , imageBase + COMPLIMENT_CARD_PATH);
    }

}