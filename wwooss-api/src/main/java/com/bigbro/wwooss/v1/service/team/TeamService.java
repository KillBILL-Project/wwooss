package com.bigbro.wwooss.v1.service.team;

import com.bigbro.wwooss.v1.dto.request.team.JoinResultRequest;
import com.bigbro.wwooss.v1.dto.request.team.CreateTeamRequest;
import com.bigbro.wwooss.v1.dto.request.team.JoinTeamRequest;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface TeamService {

    /**
     * 팀 생성 요청
     * @param userId
     * @param createTeamRequest
     * @param logo
     * @param businessRegit
     * @throws IOException
     */
    void createTeam(Long userId, CreateTeamRequest createTeamRequest, MultipartFile logo, MultipartFile businessRegit) throws IOException;

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
}
