package com.bigbro.wwooss.v1.service.team;

import com.bigbro.wwooss.v1.dto.request.team.CreateTeamRequest;
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

}
