package com.bigbro.wwooss.v1.service.team;

import com.bigbro.wwooss.v1.dto.request.team.JoinResultRequest;
import com.bigbro.wwooss.v1.dto.request.team.TeamInfoRequest;
import com.bigbro.wwooss.v1.dto.request.team.JoinTeamRequest;
import com.bigbro.wwooss.v1.dto.response.team.TeamResponse;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface TeamService {

    /**
     * 팀 생성 요청
     * @param userId
     * @param teamInfoRequest
     * @param logo
     * @param businessRegit
     * @throws IOException
     */
    void createTeam(Long userId, TeamInfoRequest teamInfoRequest, MultipartFile logo, MultipartFile businessRegit) throws IOException;

    /**
     * 승인 과정 없음 : 지금은 자유롭게 바로 가입되도록 처리
     * @param userId
     * @param joinTeamRequest
     */
    void requestJoinTeam(Long userId, JoinTeamRequest joinTeamRequest);

    /**
     * 팀 승인
     * @param requestTeamId
     * @param joinResultRequest
     */
    void approvalTeam(Long requestTeamId, JoinResultRequest joinResultRequest);

    /**
     * 나의 팀 조회
     * @param userId
     * @return
     */
    List<TeamResponse> getMyTeam(Long userId);

    /**
     * team detail
     * @param teamId
     * @param userId
     * @return
     */
    TeamResponse getTeamDetail(Long teamId, Long userId);

    /**
     * team update
     * @param teamInfoRequest
     * @param teamId
     */
    void updateTeam(TeamInfoRequest teamInfoRequest, Long teamId, Long userId,
                    MultipartFile logo) throws IOException;
}
