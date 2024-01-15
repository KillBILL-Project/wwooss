package com.bigbro.wwooss.v1.entity.complimentCard;

import com.bigbro.wwooss.v1.entity.base.BaseEntity;
import com.bigbro.wwooss.v1.entity.user.User;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.Comment;

@Table(name = "compliment_card")
@Entity
@SuperBuilder
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ComplimentCard extends BaseEntity {

    @Id
    @Column(name = "compliment_card_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long complimentCardId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "compliment_card_meta_id")
    private ComplimentCardMeta complimentCardMeta;

    @Comment("칭찬 카드 노출 여부")
    @Column(name = "show")
    @Builder.Default
    private Boolean show = true;

    public static ComplimentCard of(User user, ComplimentCardMeta complimentCardMeta) {
        return ComplimentCard.builder()
                .user(user)
                .complimentCardMeta(complimentCardMeta)
                .build();
    }

    public void updateShow(boolean show) {
        this.show = show;
    }
}
