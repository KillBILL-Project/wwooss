package com.bigbro.wwooss.v1.entity.trash.can;

import com.bigbro.wwooss.v1.dto.CarbonSavingByTrashCategory;
import com.bigbro.wwooss.v1.dto.RefundByTrashCategory;
import com.bigbro.wwooss.v1.entity.base.BaseEntity;
import com.bigbro.wwooss.v1.entity.trash.log.TrashLog;
import com.bigbro.wwooss.v1.entity.user.User;
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

    @Comment("탄소 배출 절감량")
    @Column(name = "carbon_saving")
    private Double carbonSaving;

    @Comment("환급 금액")
    @Column(name = "refund")
    private Long refund;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "trashCanHistory")
    @Builder.Default
    private List<TrashLog> trashLogList = new ArrayList<>();

    public static TrashCanHistory of(Double carbonSaving, Long refund, User user) {
        return TrashCanHistory.builder()
                .carbonSaving(carbonSaving)
                .refund(refund)
                .user(user)
                .build();
    }
}
