package com.bigbro.wwooss.v1.dto.request.team;

import com.bigbro.wwooss.v1.enumType.TeamType;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class TeamInfoRequest {
    private String teamName;
    private String address;
    private TeamType teamType;
    private String email;
    private String businessNo;
}
