package com.bigbro.wwooss.v1.dto.request.trash.can;

import lombok.*;

import javax.validation.constraints.NotNull;

@Builder
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class TrashCanContentsRequest {
    @NotNull(message = "쓰레기 정보 ID는 필수입니다.")
    private Long trashInfoId;

}
