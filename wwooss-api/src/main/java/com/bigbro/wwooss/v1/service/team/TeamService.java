package com.bigbro.wwooss.v1.service.team;

import com.bigbro.wwooss.v1.dto.request.team.CreateTeamRequest;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface TeamService {

    void createTeam(Long userId, CreateTeamRequest createTeamRequest, MultipartFile logo, MultipartFile businessRegit) throws IOException;
}
