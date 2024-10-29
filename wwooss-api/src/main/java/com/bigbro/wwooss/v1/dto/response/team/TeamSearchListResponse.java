package com.bigbro.wwooss.v1.dto.response.team;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Builder
@Getter
public class TeamSearchListResponse {
    private boolean hasNext;
    private List<TeamSearchResponse> teamSearchList;
}
