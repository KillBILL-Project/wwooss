package com.bigbro.wwooss.v1.service.team.impl;

import com.bigbro.wwooss.v1.dto.request.team.CreateTeamRequest;
import com.bigbro.wwooss.v1.entity.teamRequest.TeamRequest;
import com.bigbro.wwooss.v1.entity.team.Team;
import com.bigbro.wwooss.v1.entity.user.User;
import com.bigbro.wwooss.v1.enumType.RequestStatus;
import com.bigbro.wwooss.v1.enumType.TeamRequestType;
import com.bigbro.wwooss.v1.exception.DataNotFoundException;
import com.bigbro.wwooss.v1.repository.team.TeamRepository;
import com.bigbro.wwooss.v1.repository.teamRequest.TeamRequestRepository;
import com.bigbro.wwooss.v1.repository.user.UserRepository;
import com.bigbro.wwooss.v1.response.WwoossResponseCode;
import com.bigbro.wwooss.v1.service.team.TeamService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class TeamServiceImpl implements TeamService {

    private TeamRepository teamRepository;

    private TeamRequestRepository teamRequestRepository;

    private UserRepository userRepository;

    @Override
    @Transactional
    public void createTeam(Long userId, CreateTeamRequest createTeamRequest) {
        // 이미지 추가

        // 팀 생성
        User createUser = userRepository.findById(userId).orElseThrow(() -> new DataNotFoundException(WwoossResponseCode.NOT_FOUND_DATA, "존재하지 않는 유저입니다."));
        Team createTeam = Team.builder()
                .createBy(createUser)
                .logoImagePath("")
                .businessRegitImagePath("")
                .teamType(createTeamRequest.getTeamType())
                .address(createTeamRequest.getAddress())
                .businessNo(createTeamRequest.getBusinessNo())
                .status(RequestStatus.PENDING)
                .email(createTeamRequest.getEmail())
                .build();
        Team saveTeam = teamRepository.save(createTeam);

        // 요청 생성
        TeamRequest teamRequest = TeamRequest.builder()
                .requestUser(createUser)
                .receiveTeam(saveTeam)
                .status(RequestStatus.PENDING)
                .teamRequestType(TeamRequestType.CREATE)
                .build();
        teamRequestRepository.save(teamRequest);
    }
}
