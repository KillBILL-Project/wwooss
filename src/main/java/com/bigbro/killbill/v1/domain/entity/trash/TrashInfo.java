package com.bigbro.killbill.v1.domain.entity.trash;

import com.bigbro.killbill.v1.domain.entity.base.BaseEntity;
import com.bigbro.killbill.v1.domain.request.trash.info.TrashInfoRequest;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.Comment;

import javax.persistence.*;

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

    @Comment("쓰레기 크기")
    @Column(name = "size")
    private Integer size;

    @Comment("쓰레기 무게 (gram)")
    @Column(name = "weight")
    private Double weight;

    @Comment("그램당 탄소 배출량")
    @Column(name = "carbon_emission_per_gram")
    private Double carbonEmissionPerGram;

    @Comment("환급 금액")
    @Column(name = "refund")
    private Integer refund;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "trash_category_id")
    private TrashCategory trashCategory;

    public static TrashInfo of(TrashInfoRequest trashInfoRequest, TrashCategory trashCategory) {
        return TrashInfo.builder()
                .name(trashInfoRequest.getName())
                .size(trashInfoRequest.getSize())
                .weight(trashInfoRequest.getWeight())
                .carbonEmissionPerGram(trashInfoRequest.getCarbonEmissionPerGram())
                .refund(trashInfoRequest.getRefund())
                .trashCategory(trashCategory)
                .build();
    }

}
