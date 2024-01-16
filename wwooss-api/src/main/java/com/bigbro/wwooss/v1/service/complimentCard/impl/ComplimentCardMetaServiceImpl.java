package com.bigbro.wwooss.v1.service.complimentCard.impl;

import com.bigbro.wwooss.v1.dto.request.complimentCard.ComplimentCardMetaRequest;
import com.bigbro.wwooss.v1.entity.complimentCard.ComplimentCardMeta;
import com.bigbro.wwooss.v1.repository.complimentCard.ComplimentCardMetaRepository;
import com.bigbro.wwooss.v1.service.complimentCard.ComplimentCardMetaService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class ComplimentCardMetaServiceImpl implements ComplimentCardMetaService {

    private final ComplimentCardMetaRepository complimentCardMetaRepository;

    @Override
    @Transactional
    public void createComplimentCardMeta(ComplimentCardMetaRequest complimentCardMetaRequest) {
        complimentCardMetaRepository.save(ComplimentCardMeta.of(
                complimentCardMetaRequest.getTitle(),
                complimentCardMetaRequest.getContents(),
                complimentCardMetaRequest.getCardType(),
                complimentCardMetaRequest.getCardCode(),
                complimentCardMetaRequest.getCardImage()
        ));
    }
}
