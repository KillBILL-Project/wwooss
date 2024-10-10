package com.bigbro.wwooss.v1.dto.request.team;

import com.bigbro.wwooss.v1.enumType.TeamType;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CreateTeamRequest {
    private String teamName;
    private String address;
    private String logImagePath;
    private String email;
    private String businessNo;
    private String businessRegitImagePath;
    private TeamType teamType;
}
