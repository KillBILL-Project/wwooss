package com.bigbro.wwooss.v1.entity.trash.can;

import com.bigbro.wwooss.v1.entity.base.BaseEntity;

import javax.persistence.*;

import com.bigbro.wwooss.v1.enumType.TrashType;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.Comment;

import java.util.List;

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
    @Column(name = "trash_can_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long trashCanId;

    @Comment("쓰레기 위치 - lng")
    @Column(name = "lng")
    private Double lng;

    @Comment("쓰레기 위치 - lat")
    @Column(name = "lat")
    private Double lat;

    @Comment("쓰레기 위치 - 주소")
    @Column(name = "address")
    private String address;

    @Comment("Success Insert to ES")
    @Column(name = "insert_es")
    @Builder.Default
    private Boolean insertEs = false;

    @Comment("쓰레기 종류")
    @Column(name = "trash_type")
    private String trashType;

}
