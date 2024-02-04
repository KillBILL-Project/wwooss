package com.bigbro.wwooss.v1.service.compliment.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;
import static org.mockito.BDDMockito.given;

import com.bigbro.wwooss.v1.entity.complimentCard.ComplimentCard;
import com.bigbro.wwooss.v1.entity.complimentCard.ComplimentCardMeta;
import com.bigbro.wwooss.v1.entity.user.User;
import com.bigbro.wwooss.v1.enumType.CardCode;
import com.bigbro.wwooss.v1.enumType.CardType;
import com.bigbro.wwooss.v1.enumType.LoginType;
import com.bigbro.wwooss.v1.repository.complimentCard.ComplimentCardRepository;
import com.bigbro.wwooss.v1.repository.user.UserRepository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;

@ExtendWith(MockitoExtension.class)
public class ComplementCardImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private ComplimentCardRepository complimentCardRepository;

    @Test
    @DisplayName("주간 리포트 가져오기")
    void getComplimentCardList() {
        User user = User.builder()
                .userId(1L)
                .loginType(LoginType.GOOGLE)
                .createdAt(LocalDateTime.of(2023, 10, 24, 13, 10))
                .build();
        ComplimentCardMeta complimentCardMeta = ComplimentCardMeta.of("card meta", "card", CardType.WEEKLY,
                CardCode.login_30, "image");

        Slice<ComplimentCard> complimentCardList = new SliceImpl<>(List.of(ComplimentCard.builder()
                .complimentCardId(1L)
                .user(user)
                .complimentCardMeta(complimentCardMeta)
                .expire(false)
                .build()), PageRequest.of(10, 1),
                false);

        given(userRepository.findById(1L)).willReturn(Optional.ofNullable(user));
        given(complimentCardRepository.findByUserAndShowYAndCardType(user, false,
                        CardType.WEEKLY, PageRequest.of(10, 1))).willReturn(complimentCardList);

        Slice<ComplimentCard> getComplimentCard = complimentCardRepository.findByUserAndShowYAndCardType(user,
                false,
                CardType.WEEKLY, PageRequest.of(10, 1));

        assertThat(userRepository.findById(1L)).isNotNull();
        assertThat(getComplimentCard.getContent().size()).isNotZero();

        assertThat(getComplimentCard.getContent())
                .extracting("complimentCardId", "expire", "user", "complimentCardMeta")
                .contains(
                        tuple(1L, false, user, complimentCardMeta)
                );
    }
}
