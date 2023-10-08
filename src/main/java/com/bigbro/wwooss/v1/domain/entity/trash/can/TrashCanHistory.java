package com.bigbro.wwooss.v1.domain.entity.trash.can;

import com.bigbro.wwooss.v1.domain.entity.base.BaseEntity;
import com.bigbro.wwooss.v1.domain.entity.trash.log.TrashLog;
import com.bigbro.wwooss.v1.domain.entity.user.User;
import org.hibernate.annotations.Comment;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "trash_can_history")
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
    private Integer refund;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "trash_log")
    private List<TrashLog> trashLogList = new ArrayList<>();
}
