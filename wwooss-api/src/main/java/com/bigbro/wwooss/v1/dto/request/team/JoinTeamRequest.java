package com.bigbro.wwooss.v1.dto.request.team;

import com.bigbro.wwooss.v1.enumType.TeamType;
import lombok.Builder;
import lombok.Getter;

import javax.validation.constraints.NotNull;

@Builder
@Getter
public class JoinTeamRequest {

    @NotNull(message = "요청하고자 하는 팀 ID는 필수 입니다.")
    private Long teamId;
    @NotNull(message = "팀 타입은 필수값입니다. [SCHOOL/COMPANY/CREW/REGION]")
    private TeamType teamType;
    private String companyCode;
}
