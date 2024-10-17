package com.bigbro.wwooss.v1.api.team;

import com.bigbro.wwooss.v1.dto.request.team.TeamInfoRequest;
import com.bigbro.wwooss.v1.dto.request.team.JoinTeamRequest;
import com.bigbro.wwooss.v1.dto.request.user.UserCredential;
import com.bigbro.wwooss.v1.dto.request.team.JoinResultRequest;
import com.bigbro.wwooss.v1.dto.response.team.TeamResponse;
import com.bigbro.wwooss.v1.enumType.TeamType;
import com.bigbro.wwooss.v1.response.WwoossResponse;
import com.bigbro.wwooss.v1.response.WwoossResponseUtil;
import com.bigbro.wwooss.v1.service.team.TeamService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

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

        TeamInfoRequest teamInfoRequest = TeamInfoRequest.builder()
                .teamName(teamName)
                .address(address)
                .email(email)
                .businessNo(businessNo)
                .teamType(teamType)
                .build();
        teamService.createTeam(userCredential.getUserId(), teamInfoRequest, logoFile, businessRegitFile);
        return WwoossResponseUtil.responseCreatedNoData();
    }

    // team join
    @PostMapping("/request-join")
    public ResponseEntity<WwoossResponse<Void>> requestJoin(@RequestBody JoinTeamRequest requestJoinTeam, UserCredential userCredential) {
        teamService.requestJoinTeam(userCredential.getUserId(), requestJoinTeam);
        return WwoossResponseUtil.responseCreatedNoData();
    }

    // 승인, 거절
    @PatchMapping("/status/{team-request-id}")
    public ResponseEntity<WwoossResponse<Void>> approvalRequestTeam(@RequestBody JoinResultRequest joinResultRequest, @PathVariable("team-request-id") Long teamRequestId) {
        teamService.approvalTeam(teamRequestId, joinResultRequest);
        return WwoossResponseUtil.responseOkNoData();
    }

    // 나의 팀 조회
    @GetMapping
    public ResponseEntity<WwoossResponse<List<TeamResponse>>> getMyTeam(UserCredential userCredential) {
        return WwoossResponseUtil.responseOkAddData(teamService.getMyTeam(userCredential.getUserId()));
    }

    // 나의 팀 디테일
    @GetMapping("/{team-id}")
    public ResponseEntity<WwoossResponse<TeamResponse>> getTeamDetail(@PathVariable("team-id") Long teamId, UserCredential userCredential) {
        return WwoossResponseUtil.responseOkAddData(teamService.getTeamDetail(teamId, userCredential.getUserId()));
    }

    // 팀 정보 업데이트
    @PatchMapping("/{team-id}")
    public ResponseEntity<WwoossResponse<Void>> updateTeam(@PathVariable("team-id") Long teamId,
                                                           UserCredential userCredential,
                                                           @RequestParam(name = "teamType") TeamType teamType,
                                                           @RequestParam(name = "email") String email,
                                                           @RequestParam(name = "logoFile", required = false) MultipartFile logoFile) throws IOException {
        TeamInfoRequest teamInfoRequest = TeamInfoRequest.builder()
                .email(email)
                .teamType(teamType)
                .build();
        teamService.updateTeam(teamInfoRequest, teamId, userCredential.getUserId(), logoFile);
        return WwoossResponseUtil.responseOkNoData();
    }

    // 팀별 검색


}
