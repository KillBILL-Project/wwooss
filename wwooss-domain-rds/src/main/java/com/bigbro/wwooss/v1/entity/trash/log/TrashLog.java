package com.bigbro.wwooss.v1.entity.trash.log;

import com.bigbro.wwooss.v1.entity.base.BaseEntity;
import com.bigbro.wwooss.v1.entity.trash.can.TrashCanHistory;
import com.bigbro.wwooss.v1.entity.trash.info.TrashInfo;
import com.bigbro.wwooss.v1.entity.user.User;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.Comment;

import javax.persistence.*;

@Entity
@Table(name = "trash_log")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SuperBuilder
public class TrashLog extends BaseEntity {

    @Id
    @Column(name = "trash_log_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long trashLogId;

    @Comment("버린 쓰레기 수")
    @Column(name = "trash_count")
    private Long trashCount;

    @Comment("쓰레기 크기 - 10 단위")
    @Column(name = "size")
    private Integer size;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "trash_info_id")
    private TrashInfo trashInfo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "trash_can_history_id")
    private TrashCanHistory trashCanHistory;

    public static TrashLog of(User user, TrashInfo trashInfo, Long trashCount, Integer size) {
        return TrashLog.builder()
                .user(user)
                .trashInfo(trashInfo)
                .trashCount(trashCount)
                .size(size)
                .build();
    }

    public void updateTrashHistory(TrashCanHistory trashCanHistory) {
        this.trashCanHistory = trashCanHistory;
    }

}
