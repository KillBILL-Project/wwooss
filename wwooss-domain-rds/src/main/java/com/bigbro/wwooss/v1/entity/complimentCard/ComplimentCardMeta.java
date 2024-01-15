package com.bigbro.wwooss.v1.entity.complimentCard;

import com.bigbro.wwooss.v1.entity.base.BaseEntity;
import com.bigbro.wwooss.v1.enumType.CardCode;
import com.bigbro.wwooss.v1.enumType.CardType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.Comment;

@Table(name = "compliment_card_meta")
@Entity
@SuperBuilder
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ComplimentCardMeta extends BaseEntity {

    @Id
    @Column(name = "compliment_card_meta_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long complimentCardMetaId;

    @Comment("칭찬 카드 제목")
    @Column(name = "title")
    private String title;

    @Comment("칭찬 카드 내용")
    @Column(name = "contents")
    private String contents;

    @Comment("칭찬 카드 타입 - 주간 / 통합")
    @Column(name = "card_type")
    @Enumerated(value = EnumType.STRING)
    private CardType cardType;

    @Comment("칭찬 카드 코드")
    @Column(name = "card_code")
    @Enumerated(value = EnumType.STRING)
    private CardCode cardCode;

    @Comment("칭찬 카드 이미지")
    @Column(name = "card_image")
    private String cardImage;

    public static ComplimentCardMeta of(String title, String contents, CardType cardType,
            CardCode cardCode, String cardImage) {
        return ComplimentCardMeta.builder()
                .title(title)
                .contents(contents)
                .cardCode(cardCode)
                .cardType(cardType)
                .cardImage(cardImage)
                .build();
    }
}
