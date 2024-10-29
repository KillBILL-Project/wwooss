package com.bigbro.wwooss.v1.dto.response.team;

import com.bigbro.wwooss.v1.enumType.TeamType;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class TeamSearchResponse {
    private Long teamId;
    private String teamName;
    private String address;
    private TeamType teamType;
}
