package com.bigbro.wwooss.v1.service.team.impl;

import com.bigbro.wwooss.v1.dto.request.team.CreateTeamRequest;
import com.bigbro.wwooss.v1.entity.teamRequest.TeamRequest;
import com.bigbro.wwooss.v1.entity.team.Team;
import com.bigbro.wwooss.v1.entity.user.User;
import com.bigbro.wwooss.v1.enumType.RequestStatus;
import com.bigbro.wwooss.v1.enumType.TeamRequestType;
import com.bigbro.wwooss.v1.enumType.TeamType;
import com.bigbro.wwooss.v1.exception.DataNotFoundException;
import com.bigbro.wwooss.v1.exception.NoDataException;
import com.bigbro.wwooss.v1.repository.team.TeamRepository;
import com.bigbro.wwooss.v1.repository.teamRequest.TeamRequestRepository;
import com.bigbro.wwooss.v1.repository.user.UserRepository;
import com.bigbro.wwooss.v1.response.WwoossResponseCode;
import com.bigbro.wwooss.v1.service.s3.S3Service;
import com.bigbro.wwooss.v1.service.team.TeamService;
import com.bigbro.wwooss.v1.utils.WwoossUtil;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Objects;

import static com.bigbro.wwooss.v1.response.WwoossResponseCode.NO_DATA;
import static com.bigbro.wwooss.v1.utils.ImageUtil.TEAM_BUSINESS_REGIT;
import static com.bigbro.wwooss.v1.utils.ImageUtil.TEAM_LOGO;

@Service
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class TeamServiceImpl implements TeamService {

    private final TeamRepository teamRepository;

    private final TeamRequestRepository teamRequestRepository;

    private final UserRepository userRepository;

    private final S3Service s3Service;

    private final WwoossUtil wwoossUtil;

    @Override
    @Transactional
    public void createTeam(Long userId, CreateTeamRequest createTeamRequest, MultipartFile logo, MultipartFile businessRegit) throws IOException {
        checkValidRequest(createTeamRequest, logo, businessRegit);

        // 이미지 추가
        String teamLogoImage = s3Service.s3Upload(logo, TEAM_LOGO);
        String teamBusinessRegitImage = null;
        if(Objects.nonNull(businessRegit)) {
            teamBusinessRegitImage = s3Service.s3Upload(businessRegit, TEAM_BUSINESS_REGIT);
        }

        // 팀 생성
        User createUser = userRepository.findById(userId).orElseThrow(() -> new DataNotFoundException(WwoossResponseCode.NOT_FOUND_DATA, "존재하지 않는 유저입니다."));
        String companyCode = null;
        if(createTeamRequest.getTeamType().equals(TeamType.COMPANY)) {
            companyCode = wwoossUtil.makeRandomPw(15);
        }
        Team createTeam = Team.builder()
                .createBy(createUser)
                .logoImagePath(teamLogoImage)
                .businessRegitImagePath(teamBusinessRegitImage)
                .teamType(createTeamRequest.getTeamType())
                .address(createTeamRequest.getAddress())
                .businessNo(createTeamRequest.getBusinessNo())
                .companyCode(companyCode)
                .status(RequestStatus.PENDING)
                .email(createTeamRequest.getEmail())
                .teamName(createTeamRequest.getTeamName())
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

    private void checkValidRequest(CreateTeamRequest createTeamRequest, MultipartFile logo, MultipartFile businessRegit) {
        // 공통 필수값
        if(StringUtils.isBlank(createTeamRequest.getTeamName())
                || Objects.isNull(logo)) {
            throw new NoDataException(NO_DATA, "팀 이름, 로고는 필수 값입니다.");
        }

        // 회사 필수값
        if(createTeamRequest.getTeamType().equals(TeamType.COMPANY)
                && (StringUtils.isBlank(createTeamRequest.getBusinessNo()) || Objects.isNull(businessRegit))) {
            throw new NoDataException(NO_DATA, "회사인 경우 사업자 번호, 사업자 등록증 필수 입니다.");
        }

        // 크루, 학교 공통 필수값
        if((createTeamRequest.getTeamType().equals(TeamType.CREW) || createTeamRequest.getTeamType().equals(TeamType.SCHOOL))
                && StringUtils.isBlank(createTeamRequest.getEmail())) {
            throw new NoDataException(NO_DATA, "email은 필수입니다..");
        }

        // 회사, 학교 공통 필수
        if((createTeamRequest.getTeamType().equals(TeamType.COMPANY) || createTeamRequest.getTeamType().equals(TeamType.SCHOOL)) && StringUtils.isBlank(createTeamRequest.getAddress())){
            throw new NoDataException(NO_DATA, "주소는 필수 입니다.");
        }

        // 회사가 아닌데 사업자 관련 정보 있을 때
        if(!createTeamRequest.getTeamType().equals(TeamType.COMPANY) && (!StringUtils.isBlank(createTeamRequest.getBusinessNo())
        || !Objects.isNull(businessRegit))) {
            throw new NoDataException(NO_DATA, "사업자번호, 사업자 등록증은 기업 타입에만 입력해주세요");
        }
    }


}
