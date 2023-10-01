package com.bigbro.killbill.v1.domain.request.trash.info;

import lombok.*;

import javax.validation.constraints.NotNull;

@Builder
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class TrashInfoRequest {

    @NotNull(message = "쓰레기 이름은 필수입니다.")
    private String name;

    @NotNull(message = "쓰레기 크기는 필수입니다.")
    private Integer size;

    @NotNull(message = "쓰레기 이름은 필수입니다.")
    private Double weight;

    @NotNull(message = "쓰레기 이름은 필수입니다.")
    private Double carbonEmissionPerGram;

    @NotNull(message = "쓰레기 이름은 필수입니다.")
    private Integer refund;

    @NotNull(message = "쓰레기 카테고리 필수입니다.")
    private Long trashCategoryId;

}
