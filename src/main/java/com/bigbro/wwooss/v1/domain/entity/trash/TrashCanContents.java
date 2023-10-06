package com.bigbro.wwooss.v1.domain.entity.trash;

import com.bigbro.wwooss.v1.domain.entity.base.BaseEntity;
import com.bigbro.wwooss.v1.domain.entity.user.User;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.Comment;

import javax.persistence.*;

/**
 * 쓰레기통 로그를 이용 안하고 별도로 쓰레기통 내용물 테이블을 둔 이유
 * 로그를 이용하게 될 경우 이미 비워진 쓰레기 내용물까지 포함하고 있어
 * 나중에 많은 쓰레기 로그가 존재할 경우 조회 성능에 문제 생길걸 대비하여
 * 현 쓰레기통 상태값 만을 위한 값을 별도로 둠.
 */

@Entity
@Table(name = "trash_can_contents")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SuperBuilder
public class TrashCanContents extends BaseEntity {

    @Id
    @Column(name = "trash_can_contents_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long trashCanContentsId;

    @Comment("버린 쓰레기 수")
    @Column(name = "trash_count")
    private Long trashCount;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @OneToOne
    @JoinColumn(name = "trash_info_id")
    private TrashInfo trashInfo;

    public static TrashCanContents of(TrashInfo trashInfo, User user, Long trashCount) {
        return TrashCanContents.builder()
                .trashCount(trashCount)
                .user(user)
                .trashInfo(trashInfo)
                .build();
    }
}
