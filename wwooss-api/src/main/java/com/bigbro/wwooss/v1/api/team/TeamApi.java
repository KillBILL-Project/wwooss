package com.bigbro.wwooss.v1.api.team;

import com.bigbro.wwooss.v1.dto.request.team.CreateTeamRequest;
import com.bigbro.wwooss.v1.dto.request.user.UserCredential;
import com.bigbro.wwooss.v1.enumType.TeamType;
import com.bigbro.wwooss.v1.response.WwoossResponse;
import com.bigbro.wwooss.v1.response.WwoossResponseUtil;
import com.bigbro.wwooss.v1.service.team.TeamService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/v1/team")
@RequiredArgsConstructor
public class TeamApi {

    private final TeamService teamService;

    // team 생성
    @PostMapping
    public ResponseEntity<WwoossResponse<Void>> createTeam(
            @RequestParam(name = "logoFile") MultipartFile logoFile,
            @RequestParam(name = "teamName") String teamName,
            @RequestParam(name = "address") String address,
            @RequestParam(name = "teamType") TeamType teamType,
            @RequestParam(name = "email", required = false) String email,
            @RequestParam(name = "businessNo", required = false) String businessNo,
            @RequestParam(name = "businessRegitFile", required = false) MultipartFile businessRegitFile,
            UserCredential userCredential) throws IOException {

        CreateTeamRequest createTeamRequest = CreateTeamRequest.builder()
                .teamName(teamName)
                .address(address)
                .email(email)
                .businessNo(businessNo)
                .teamType(teamType)
                .build();
        teamService.createTeam(userCredential.getUserId(), createTeamRequest, logoFile, businessRegitFile);
        return WwoossResponseUtil.responseCreatedNoData();
    }

    // team join

    // 승인, 거절, 대기

    // 나의 팀 조회

    // 팀별 검색

    // 나의 팀 디테일
}
