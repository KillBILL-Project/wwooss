package com.bigbro.wwooss.v1.entity.trash.can;

import com.bigbro.wwooss.v1.entity.base.BaseEntity;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.Comment;

/**
 * 쓰레기 위치 정보 및 기본 정보
 * ES와 매칭 테이블
 */

@Table(name = "trash_can")
@Entity
@SuperBuilder
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TrashCan extends BaseEntity {

    @Id
    @Column(name = "trash_can")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long trashCanId;

    @Comment("쓰레기 위치 - lng")
    @Column(name = "lng")
    private Long lng;

    @Comment("쓰레기 위치 - lat")
    @Column(name = "lat")
    private Long lat;

    @Comment("쓰레기 위치 - 주소")
    @Column(name = "address")
    private String address;

    @Comment("쓰레기 종류")
    @Column(name = "trash_category")
    private String trashCategory;

}
