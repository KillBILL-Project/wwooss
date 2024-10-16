package com.bigbro.wwooss.v1.dto.request.team;

import com.bigbro.wwooss.v1.enumType.RequestStatus;
import com.bigbro.wwooss.v1.enumType.TeamRequestType;
import lombok.Builder;
import lombok.Getter;

import javax.validation.constraints.NotNull;

@Getter
@Builder
public class JoinResultRequest {
    @NotNull(message = "요청에 대한 결과값을 입력해주세요 - APPROVE/REJECT")
    private RequestStatus status;
    private String rejectReason;
}
