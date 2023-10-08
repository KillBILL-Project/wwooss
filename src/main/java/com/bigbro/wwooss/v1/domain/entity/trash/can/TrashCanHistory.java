package com.bigbro.wwooss.v1.domain.entity.trash.can;

import com.bigbro.wwooss.v1.domain.entity.base.BaseEntity;
import com.bigbro.wwooss.v1.domain.entity.trash.log.TrashLog;
import com.bigbro.wwooss.v1.domain.entity.user.User;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.Comment;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "trash_can_history")
@SuperBuilder
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TrashCanHistory extends BaseEntity {

    @Id
    @Column(name = "trash_can_history_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long trashCanHistoryId;

    @Comment("탄소 배출량")
    @Column(name = "carbon_emission")
    private Double carbonEmission;

    @Comment("환급 금액")
    @Column(name = "refund")
    private Long refund;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "trashCanHistory")
    @Builder.Default
    private List<TrashLog> trashLogList = new ArrayList<>();

    public static TrashCanHistory of(Double carbonEmission, Long refund, User user, List<TrashLog> trashLogList) {
        return TrashCanHistory.builder()
                .carbonEmission(carbonEmission)
                .refund(refund)
                .user(user)
                .trashLogList(trashLogList)
                .build();
    }
}
