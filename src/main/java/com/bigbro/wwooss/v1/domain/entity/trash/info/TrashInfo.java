package com.bigbro.wwooss.v1.domain.entity.trash.info;

import com.bigbro.wwooss.v1.domain.entity.base.BaseEntity;
import com.bigbro.wwooss.v1.domain.entity.trash.category.TrashCategory;
import com.bigbro.wwooss.v1.domain.request.trash.info.TrashInfoRequest;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.Comment;

import javax.persistence.*;

/**
 * 쓰레기 정보 기준은 최소 10으로 해서 메타 데이터 입력
 * 아래의 값은 기준 10으로 적용
 * log, contents에 size 값 기입 => 10을 기준으로 size * 10
 */

@Entity
@Table(name = "trash_info")
@SuperBuilder
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TrashInfo extends BaseEntity {

    @Id
    @Column(name = "trash_info_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long trashInfoId;

    @Comment("쓰레기 이름")
    @Column(name = "name")
    private String name;

    @Comment("쓰레기 무게 (gram)")
    @Column(name = "weight")
    private Double weight;

    @Comment("표준 탄소 배출량 - 10 단위 기준")
    @Column(name = "standard_carbon_emission")
    private Double standardCarbonEmission;

    @Comment("환급 금액")
    @Column(name = "refund")
    private Long refund;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "trash_category_id")
    private TrashCategory trashCategory;

    public static TrashInfo of(TrashInfoRequest trashInfoRequest, TrashCategory trashCategory) {
        return TrashInfo.builder()
                .name(trashInfoRequest.getName())
                .weight(trashInfoRequest.getWeight())
                .standardCarbonEmission(trashInfoRequest.getStandardCarbonEmission())
                .refund(trashInfoRequest.getRefund())
                .trashCategory(trashCategory)
                .build();
    }

}
