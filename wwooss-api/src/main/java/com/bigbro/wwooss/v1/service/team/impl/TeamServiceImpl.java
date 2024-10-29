package com.bigbro.wwooss.v1.service.team.impl;

import com.bigbro.wwooss.v1.dto.request.team.JoinResultRequest;
import com.bigbro.wwooss.v1.dto.request.team.TeamInfoRequest;
import com.bigbro.wwooss.v1.dto.request.team.JoinTeamRequest;
import com.bigbro.wwooss.v1.dto.response.team.TeamResponse;
import com.bigbro.wwooss.v1.dto.response.team.TeamSearchListResponse;
import com.bigbro.wwooss.v1.dto.response.team.TeamSearchResponse;
import com.bigbro.wwooss.v1.entity.team.Team;
import com.bigbro.wwooss.v1.entity.teamRequest.TeamRequest;
import com.bigbro.wwooss.v1.entity.teamUser.TeamUser;
import com.bigbro.wwooss.v1.entity.user.User;
import com.bigbro.wwooss.v1.enumType.RequestStatus;
import com.bigbro.wwooss.v1.enumType.TeamRequestType;
import com.bigbro.wwooss.v1.enumType.TeamType;
import com.bigbro.wwooss.v1.exception.DataNotFoundException;
import com.bigbro.wwooss.v1.exception.DuplicationException;
import com.bigbro.wwooss.v1.exception.IncorrectDataException;
import com.bigbro.wwooss.v1.exception.NoDataException;
import com.bigbro.wwooss.v1.repository.team.TeamRepository;
import com.bigbro.wwooss.v1.repository.team.TeamRequestRepository;
import com.bigbro.wwooss.v1.repository.team.TeamUserRepository;
import com.bigbro.wwooss.v1.repository.user.UserRepository;
import com.bigbro.wwooss.v1.response.WwoossResponseCode;
import com.bigbro.wwooss.v1.service.s3.S3Service;
import com.bigbro.wwooss.v1.service.team.TeamService;
import com.bigbro.wwooss.v1.utils.ImageUtil;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

import static com.bigbro.wwooss.v1.response.WwoossResponseCode.*;
import static com.bigbro.wwooss.v1.utils.ImageUtil.TEAM_BUSINESS_REGIT;
import static com.bigbro.wwooss.v1.utils.ImageUtil.TEAM_LOGO;

@Service
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class TeamServiceImpl implements TeamService {

    private final TeamRepository teamRepository;

    private final TeamRequestRepository teamRequestRepository;

    private final UserRepository userRepository;

    private final TeamUserRepository teamUserRepository;

    private final S3Service s3Service;

    private final ImageUtil imageUtil;

    @Override
    @Transactional
    public void createTeam(Long userId, TeamInfoRequest createTeamInfoRequest, MultipartFile logo, MultipartFile businessRegit) throws IOException {
        checkValidRequest(createTeamInfoRequest, logo, businessRegit);
        checkDuplication(createTeamInfoRequest);

        // 이미지 추가
        String teamLogoImage = s3Service.s3Upload(logo, TEAM_LOGO);
        String teamBusinessRegitImage = null;
        if(Objects.nonNull(businessRegit)) {
            teamBusinessRegitImage = s3Service.s3Upload(businessRegit, TEAM_BUSINESS_REGIT);
        }

        // 팀 생성
        User createUser = userRepository.findById(userId).orElseThrow(() -> new DataNotFoundException(WwoossResponseCode.NOT_FOUND_DATA, "존재하지 않는 유저입니다."));
        Team createTeam = Team.builder()
                .createBy(createUser)
                .logoImagePath(teamLogoImage)
                .businessRegitImagePath(teamBusinessRegitImage)
                .teamType(createTeamInfoRequest.getTeamType())
                .address(createTeamInfoRequest.getAddress())
                .businessNo(createTeamInfoRequest.getBusinessNo())
                .status(RequestStatus.PENDING)
                .email(createTeamInfoRequest.getEmail())
                .teamName(createTeamInfoRequest.getTeamName())
                .build();
        Team createdTeam = teamRepository.save(createTeam);

        // 요청 생성
        TeamRequest teamRequest = TeamRequest.builder()
                .teamRequestType(TeamRequestType.CREATE)
                .team(createdTeam)
                .status(RequestStatus.PENDING)
                .requestUser(createUser)
                .build();
        teamRequestRepository.save(teamRequest);
    }

    @Override
    @Transactional
    public void requestJoinTeam(Long userId, JoinTeamRequest joinTeamRequest) {
        User requestUser = userRepository.findById(userId).orElseThrow(() -> new DataNotFoundException(WwoossResponseCode.NOT_FOUND_DATA, "존재하지 않는 유저입니다."));
        Team team = teamRepository.findById(joinTeamRequest.getTeamId()).orElseThrow(() -> new DataNotFoundException(WwoossResponseCode.NOT_FOUND_DATA, "존재하지 팀입니다."));

        com.bigbro.wwooss.v1.entity.teamRequest.TeamRequest teamRequest = com.bigbro.wwooss.v1.entity.teamRequest.TeamRequest.builder()
                .requestUser(requestUser)
                .team(team)
                .status(RequestStatus.APPROVE) // join은 자동 승인 : 추후 승인 과정 넣을 예정
                .teamRequestType(TeamRequestType.JOIN)
                .build();
        teamRequestRepository.save(teamRequest);
    }

    @Override
    @Transactional
    public void approvalTeam(Long requestTeamId, JoinResultRequest joinResultRequest) {
        TeamRequest teamRequest = teamRequestRepository.findById(requestTeamId).orElseThrow(() -> new DataNotFoundException(WwoossResponseCode.NOT_FOUND_DATA, "존재하지 않는 요청입니다."));

        //거절
        if(joinResultRequest.getStatus().equals(RequestStatus.REJECT)) {
            teamRequest.rejectResult(joinResultRequest.getRejectReason());
            teamRequest.getTeam().updateStatus(RequestStatus.REJECT);
            return;
        }

        User requestUser = teamRequest.getRequestUser();
        Team team = teamRequest.getTeam();

        // 생성 승인
        teamRequest.approveResult();
        team.updateStatus(RequestStatus.APPROVE);

        // 생성된 팀에 요청자 주입
        TeamUser teamUser = TeamUser.builder()
                .team(team)
                .user(requestUser)
                .build();
        teamUserRepository.save(teamUser);
    }

    @Override
    @Transactional(readOnly = true)
    public List<TeamResponse> getMyTeam(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new DataNotFoundException(WwoossResponseCode.NOT_FOUND_DATA, "존재하지 않는 유저입니다."));
        List<TeamUser> teamUsers = teamUserRepository.findByUser(user);
        return teamUsers.stream().map((teamUser) -> TeamResponse.builder()
                .teamId(teamUser.getTeam().getTeamId())
                .teamType(teamUser.getTeam().getTeamType())
                .teamName(teamUser.getTeam().getTeamName())
                .status(teamUser.getTeam().getStatus())
                .owner(teamUser.getTeam().getCreateBy().getUserId().equals(user.getUserId()))
                .logoImage(imageUtil.baseTeamLogo() + "/" + teamUser.getTeam().getLogoImagePath())
                .build()
        ).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public TeamResponse getTeamDetail(Long teamId, Long userId) {
        Team team = teamRepository.findById(teamId).orElseThrow(() -> new DataNotFoundException(NOT_FOUND_DATA, "존재하지 않는 팀입니다."));
        boolean isOwner = team.getCreateBy().getUserId().equals(userId);

        return TeamResponse.builder()
                .teamId(team.getTeamId())
                .teamType(team.getTeamType())
                .teamName(team.getTeamName())
                .status(team.getStatus())
                .owner(isOwner)
                .logoImage(imageUtil.baseTeamLogo() + "/" + team.getLogoImagePath())
                .build();
    }

    @Override
    @Transactional
    public void updateTeam(TeamInfoRequest teamInfoRequest, Long teamId, Long userId,
                           MultipartFile logo) throws IOException {
        Team team = teamRepository.findByTeamIdAndTeamType(teamId, teamInfoRequest.getTeamType()).orElseThrow(() -> new DataNotFoundException(NOT_FOUND_DATA, "존재하지 않는 팀입니다."));
        if(!team.getCreateBy().getUserId().equals(userId)) {
            throw new IncorrectDataException(INVALID_DATA, "팀의 주인이 아닙니다.");
        }

        // 이미지 변경
        if(Objects.nonNull(logo)) {
            team.updateLogo(s3Service.s3Upload(logo, TEAM_LOGO));
        }

        team.updateTeamInfo(teamInfoRequest.getEmail());
    }

    @Override
    @Transactional(readOnly = true)
    public TeamSearchListResponse searchTeam(Pageable pageable, TeamType teamType, String searchText) {
        Slice<Team> searchedTeam = teamRepository.findTeamByTeamTypeAndTeamNameContaining(teamType, searchText, pageable);
        List<TeamSearchResponse> result = searchedTeam.getContent().stream()
                .map((team) -> TeamSearchResponse.builder()
                        .teamId(team.getTeamId())
                        .teamName(team.getTeamName())
                        .address(team.getAddress())
                        .teamType(team.getTeamType())
                        .build()).toList();

        return TeamSearchListResponse.builder()
                .hasNext(searchedTeam.hasNext())
                .teamSearchList(result)
                .build();
    }

    private void checkValidRequest(TeamInfoRequest teamInfoRequest, MultipartFile logo, MultipartFile businessRegit) {
        // 공통 필수값
        if(StringUtils.isBlank(teamInfoRequest.getTeamName())
                || Objects.isNull(logo)) {
            throw new NoDataException(NO_DATA, "팀 이름, 로고는 필수 값입니다.");
        }

        // 회사 필수값
        if(teamInfoRequest.getTeamType().equals(TeamType.COMPANY)
                && (StringUtils.isBlank(teamInfoRequest.getBusinessNo()) || Objects.isNull(businessRegit))) {
            throw new NoDataException(NO_DATA, "회사인 경우 사업자 번호, 사업자 등록증 필수 입니다.");
        }

        // 크루, 학교 공통 필수값
        if((teamInfoRequest.getTeamType().equals(TeamType.CREW) || teamInfoRequest.getTeamType().equals(TeamType.SCHOOL))
                && StringUtils.isBlank(teamInfoRequest.getEmail())) {
            throw new NoDataException(NO_DATA, "email은 필수입니다..");
        }

        // 회사, 학교 공통 필수
        if((teamInfoRequest.getTeamType().equals(TeamType.COMPANY) || teamInfoRequest.getTeamType().equals(TeamType.SCHOOL)) && StringUtils.isBlank(teamInfoRequest.getAddress())){
            throw new NoDataException(NO_DATA, "주소는 필수 입니다.");
        }

        // 회사가 아닌데 사업자 관련 정보 있을 때
        if(!teamInfoRequest.getTeamType().equals(TeamType.COMPANY) && (!StringUtils.isBlank(teamInfoRequest.getBusinessNo())
        || !Objects.isNull(businessRegit))) {
            throw new NoDataException(NO_DATA, "사업자번호, 사업자 등록증은 기업 타입에만 입력해주세요");
        }
    }

    private void checkDuplication(TeamInfoRequest teamInfoRequest) {
        Team team = null;
        // 기업
        // 팀명, 사업자번호 주소 이메일
        if(teamInfoRequest.getTeamType().equals(TeamType.COMPANY)) {
            team = teamRepository.findCompanyTeam(teamInfoRequest.getTeamName(), teamInfoRequest.getAddress(),
                    teamInfoRequest.getBusinessNo(), teamInfoRequest.getEmail());
        }

        // 학교
        // 팀명, 주소, 이메일
        if(teamInfoRequest.getTeamType().equals(TeamType.SCHOOL)) {
            team = teamRepository.findSchoolTeam(teamInfoRequest.getTeamName(), teamInfoRequest.getAddress(), teamInfoRequest.getEmail());
        }

        // 크루
        // 팀명, 이메일
        if(teamInfoRequest.getTeamType().equals(TeamType.CREW)) {
            team = teamRepository.findCrewTeam(teamInfoRequest.getTeamName(), teamInfoRequest.getEmail());
        }

        if(Objects.nonNull(team)) {
            throw new DuplicationException(DUPLICATION_RESOURCE);
        }
    }

}
