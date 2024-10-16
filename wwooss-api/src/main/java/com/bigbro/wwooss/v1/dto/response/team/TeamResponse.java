package com.bigbro.wwooss.v1.dto.response.team;

import com.bigbro.wwooss.v1.enumType.RequestStatus;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class TeamResponse {
    private Long teamId;
    private String teamName;
    private String logoImage;
    private RequestStatus status;
}
