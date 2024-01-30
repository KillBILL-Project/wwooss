package com.bigbro.wwooss.v1.dto.request.trash.info;

import com.bigbro.wwooss.v1.enumType.TrashSize;
import lombok.*;

import javax.validation.constraints.NotNull;

@Builder
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class TrashInfoRequest {

    @NotNull(message = "쓰레기 이름은 필수입니다.")
    private String name;

    @NotNull(message = "쓰레기 무게는 필수입니다.")
    private Double weight;

    @NotNull(message = "탄소 절감량은 필수입니다.")
    private Double carbonSaving;

    @NotNull(message = "환급금은 필수입니다.")
    private Long refund;

    @NotNull(message = "쓰레기 크기는 필수입니다.")
    private TrashSize size;

    @NotNull(message = "쓰레기 카테고리 ID 필수입니다.")
    private Long trashCategoryId;

    @NotNull(message = "쓰레기 이미지는 필수입니다.")
    private String trashImagePath;
}
