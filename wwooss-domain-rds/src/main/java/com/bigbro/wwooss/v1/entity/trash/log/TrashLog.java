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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "trash_info_id")
    private TrashInfo trashInfo;

    /**
     * 쓰레기통 비우기 실행 시 History 값이 삽입.
     * 비우기 전에는 null
     * 쓰레기 비운 이력 관리
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "trash_can_history_id")
    private TrashCanHistory trashCanHistory;

    public static TrashLog of(User user, TrashInfo trashInfo) {
        return TrashLog.builder()
                .user(user)
                .trashInfo(trashInfo)
                .build();
    }

    public void updateTrashHistory(TrashCanHistory trashCanHistory) {
        this.trashCanHistory = trashCanHistory;
    }

}
