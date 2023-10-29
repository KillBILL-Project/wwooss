package com.bigbro.wwooss.v1.entity.trash.info;

import com.bigbro.wwooss.v1.entity.base.BaseEntity;
import com.bigbro.wwooss.v1.entity.trash.category.TrashCategory;
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

    @Comment("최소 쓰레기 무게 (gram)")
    @Column(name = "weight")
    private Double weight;

    @Comment("1g당 탄소 배출량")
    @Column(name = "carbon_emission_per_gram")
    private Double carbonEmissionPerGram;

    @Comment("환급 금액")
    @Column(name = "refund")
    private Long refund;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "trash_category_id")
    private TrashCategory trashCategory;

    public static TrashInfo of(String name, Double weight, Double carbonEmissionPerGram, Long refund, TrashCategory trashCategory) {
        return TrashInfo.builder()
                .name(name)
                .weight(weight)
                .carbonEmissionPerGram(carbonEmissionPerGram)
                .refund(refund)
                .trashCategory(trashCategory)
                .build();
    }

}
