package com.bigbro.wwooss.v1.dto.response.team;

import com.bigbro.wwooss.v1.enumType.RequestStatus;
import com.bigbro.wwooss.v1.enumType.TeamType;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class TeamResponse {
    private Long teamId;
    private String teamName;
    private String logoImage;
    private RequestStatus status;
    private TeamType teamType;
    private Boolean owner;
}
