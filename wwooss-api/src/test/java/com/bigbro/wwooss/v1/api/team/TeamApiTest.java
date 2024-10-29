package com.bigbro.wwooss.v1.api.team;

import com.bigbro.wwooss.v1.annotation.TestController;
import com.bigbro.wwooss.v1.annotation.WithMockCustomUser;
import com.bigbro.wwooss.v1.config.DocumentConfig;
import com.bigbro.wwooss.v1.dto.request.team.JoinResultRequest;
import com.bigbro.wwooss.v1.dto.response.team.TeamResponse;
import com.bigbro.wwooss.v1.dto.response.team.TeamSearchListResponse;
import com.bigbro.wwooss.v1.dto.response.team.TeamSearchResponse;
import com.bigbro.wwooss.v1.enumType.RequestStatus;
import com.bigbro.wwooss.v1.enumType.TeamType;
import com.bigbro.wwooss.v1.service.team.TeamService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.test.web.servlet.MockMvc;

import java.io.FileInputStream;
import java.util.List;

import static com.epages.restdocs.apispec.MockMvcRestDocumentationWrapper.document;
import static com.epages.restdocs.apispec.MockMvcRestDocumentationWrapper.resourceDetails;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@TestController
@WebMvcTest(TeamApi.class)
class TeamApiTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TeamService teamService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    @WithMockCustomUser
    @DisplayName("팀 생성 요청")
    void createAlarm() throws Exception {
        final String fileName = "sample.jpeg";
        final String contentType = "image/jpeg";
        final String filePath = "src/test/resources/images/" + fileName;
        FileInputStream logofileInputStream = new FileInputStream(filePath);
        FileInputStream businessfileInputStream = new FileInputStream(filePath);

        MockMultipartFile logoImage = new MockMultipartFile(
                "logoFile", //name
                fileName, //originalFilename
                contentType,
                logofileInputStream
        );

        MockMultipartFile businessImage = new MockMultipartFile(
                "businessRegitFile", //name
                fileName, //originalFilename
                contentType,
                businessfileInputStream
        );

        this.mockMvc.perform(RestDocumentationRequestBuilders.multipart("/api/v1/team")
                        .file(logoImage)
                        .file(businessImage)
                        .param("teamName", "우쓰")
                        .param("address", "tkdd@dddd.com")
                        .param("email", "email")
                        .param("businessNo", "232323-2323")
                        .param("teamType", "COMPANY")
                        .with(csrf().asHeader())
                        .header("Authorization", "bearer TEST_ACCESS")
                        .contextPath("/api")
                        .contentType(MediaType.MULTIPART_FORM_DATA)
                )
                .andExpect(status().isCreated())
                .andDo(document("create-team",
                                resourceDetails().tags("Team 생성")
                                        .description("학교, 기업, 지역, 크루 공통. 불필요한 값은 null로 보내기."),
                                DocumentConfig.getDocumentRequest(),
                                DocumentConfig.getDocumentResponse(),
                                requestHeaders(
                                        headerWithName("Authorization").description("인증 토큰")
                                ),
                                requestParts(
                                        partWithName("logoFile").description("로고 파일"),
                                        partWithName("businessRegitFile").description("사업자 등록증").optional()
                                ),
                                responseFields(
                                        fieldWithPath("code").description("응답 코드"),
                                        fieldWithPath("title").description("응답 코드 별 클라이언트 노출 제목"),
                                        fieldWithPath("message").description("응답 코드 별 클라이언트 노출 메세지"),
                                        fieldWithPath("data").description("데이터 없음")
                                )
                        )
                );
    }

    @Test
    @WithMockCustomUser
    @DisplayName("팀 승인/거절")
    void approvalTeam() throws Exception {

        JoinResultRequest joinResultRequest = JoinResultRequest.builder()
                .status(RequestStatus.REJECT)
                .rejectReason("거절")
                .build();
        this.mockMvc.perform(RestDocumentationRequestBuilders.patch("/api/v1/team/status/{team-request-id}", 1L)
                        .with(csrf().asHeader())
                        .header("Authorization", "bearer TEST_ACCESS")
                        .contextPath("/api")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(joinResultRequest))
                )
                .andExpect(status().isOk())
                .andDo(document("request-approval-team",
                                resourceDetails().tags("Team 승인/거절")
                                        .description("승인/거절 - 어드민 기능 : 승인 시 rejectReason - null"),
                                DocumentConfig.getDocumentRequest(),
                                DocumentConfig.getDocumentResponse(),
                                requestHeaders(
                                        headerWithName("Authorization").description("인증 토큰")
                                ),
                                requestFields(
                                        fieldWithPath("status").description("승인/거절 : [APPROVE/REJECT]"),
                                        fieldWithPath("rejectReason").description("거절 사유").optional()
                                ),
                                responseFields(
                                        fieldWithPath("code").description("응답 코드"),
                                        fieldWithPath("title").description("응답 코드 별 클라이언트 노출 제목"),
                                        fieldWithPath("message").description("응답 코드 별 클라이언트 노출 메세지"),
                                        fieldWithPath("data").description("데이터 없음")
                                )
                        )
                );
    }

    @Test
    @WithMockCustomUser
    @DisplayName("나의 팀 조회")
    void getMyTeam() throws Exception {
        TeamResponse wwoossTeam = TeamResponse.builder()
                .teamId(1L)
                .teamType(TeamType.COMPANY)
                .teamName("WWOOSS")
                .status(RequestStatus.APPROVE)
                .logoImage("WWOOSS.png")
                .owner(true)
                .build();
        given(teamService.getMyTeam(1L)).willReturn(List.of(wwoossTeam));

        this.mockMvc.perform(RestDocumentationRequestBuilders.get("/api/v1/team")
                        .with(csrf().asHeader())
                        .header("Authorization", "bearer TEST_ACCESS")
                        .contextPath("/api")
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andDo(document("get-my-team",
                                resourceDetails().tags("나의 Team 조회")
                                        .description("나의 팀 조회 : 있는 팀만 출력"),
                                DocumentConfig.getDocumentRequest(),
                                DocumentConfig.getDocumentResponse(),
                                requestHeaders(
                                        headerWithName("Authorization").description("인증 토큰")
                                ),
                                responseFields(
                                        fieldWithPath("code").description("응답 코드"),
                                        fieldWithPath("title").description("응답 코드 별 클라이언트 노출 제목"),
                                        fieldWithPath("message").description("응답 코드 별 클라이언트 노출 메세지"),
                                        fieldWithPath("data[].teamId").description("팀 ID"),
                                        fieldWithPath("data[].teamType").description("팀 타입 - [COMPANY/SCHOOL/CREW/REGION]"),
                                        fieldWithPath("data[].teamName").description("팀 이름"),
                                        fieldWithPath("data[].logoImage").description("팀 logo"),
                                        fieldWithPath("data[].owner").description("팀 주인 여부"),
                                        fieldWithPath("data[].status").description("팀 승인 상태 - [APPROVE/REJECT/PENDING]")
                                )
                        )
                );
    }

    @Test
    @WithMockCustomUser
    @DisplayName("팀 detail")
    void getTeamDetail() throws Exception {
        TeamResponse wwoossTeam = TeamResponse.builder()
                .teamId(1L)
                .teamType(TeamType.COMPANY)
                .teamName("WWOOSS")
                .status(RequestStatus.APPROVE)
                .logoImage("WWOOSS.png")
                .owner(true)
                .build();
        given(teamService.getTeamDetail(1L, 1L)).willReturn(wwoossTeam);

        this.mockMvc.perform(RestDocumentationRequestBuilders.get("/api/v1/team/{team-id}", 1L)
                        .with(csrf().asHeader())
                        .header("Authorization", "bearer TEST_ACCESS")
                        .contextPath("/api")
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andDo(document("get-team-detail",
                                resourceDetails().tags("Team Detail 조회")
                                        .description("팀 디테일 조회"),
                                DocumentConfig.getDocumentRequest(),
                                DocumentConfig.getDocumentResponse(),
                                requestHeaders(
                                        headerWithName("Authorization").description("인증 토큰")
                                ),
                                responseFields(
                                        fieldWithPath("code").description("응답 코드"),
                                        fieldWithPath("title").description("응답 코드 별 클라이언트 노출 제목"),
                                        fieldWithPath("message").description("응답 코드 별 클라이언트 노출 메세지"),
                                        fieldWithPath("data.teamId").description("팀 ID"),
                                        fieldWithPath("data.teamType").description("팀 타입 - [COMPANY/SCHOOL/CREW/REGION]"),
                                        fieldWithPath("data.teamName").description("팀 이름"),
                                        fieldWithPath("data.logoImage").description("팀 logo"),
                                        fieldWithPath("data.owner").description("팀 주인 여부"),
                                        fieldWithPath("data.status").description("팀 승인 상태 - [APPROVE/REJECT/PENDING]")
                                )
                        )
                );
    }

    @Test
    @WithMockCustomUser
    @DisplayName("팀 update")
    void updateTeam() throws Exception {
        final String fileName = "sample.jpeg";
        final String contentType = "image/jpeg";
        final String filePath = "src/test/resources/images/" + fileName;
        FileInputStream logofileInputStream = new FileInputStream(filePath);

        MockMultipartFile logoImage = new MockMultipartFile(
                "logoFile", //name
                fileName, //originalFilename
                contentType,
                logofileInputStream
        );

        this.mockMvc.perform(RestDocumentationRequestBuilders.multipart("/api/v1/team/{team-id}", 1L)
                        .file(logoImage)
                        .param("email", "email")
                        .param("teamType", "COMPANY")
                        .with(csrf().asHeader())
                        .with(request -> {
                            request.setMethod("PATCH");
                            return request;
                        })
                        .header("Authorization", "bearer TEST_ACCESS")
                        .contextPath("/api")
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andDo(document("update-team",
                                resourceDetails().tags("Team 정보 변경")
                                        .description("team type, email 필수 변경되지 않아도 보내기, 로그는 optional"),
                                DocumentConfig.getDocumentRequest(),
                                DocumentConfig.getDocumentResponse(),
                                requestHeaders(
                                        headerWithName("Authorization").description("인증 토큰")
                                ),
                                requestParts(
                                        partWithName("logoFile").description("로고 파일").optional()
                                ),
                                responseFields(
                                        fieldWithPath("code").description("응답 코드"),
                                        fieldWithPath("title").description("응답 코드 별 클라이언트 노출 제목"),
                                        fieldWithPath("message").description("응답 코드 별 클라이언트 노출 메세지"),
                                        fieldWithPath("data").description("데이터 없음")
                                )
                        )
                );
    }

//    @Test
//    @WithMockCustomUser
//    @DisplayName("팀 합류 요청")
//    void joinTeam() throws Exception {
//
//        JoinTeamRequest joinTeamRequest = JoinTeamRequest.builder()
//                .teamType(TeamType.COMPANY)
//                .teamId(7L)
//                .build();
//        this.mockMvc.perform(RestDocumentationRequestBuilders.post("/api/v1/team/request-join")
//                        .with(csrf().asHeader())
//                        .header("Authorization", "bearer TEST_ACCESS")
//                        .contextPath("/api")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(joinTeamRequest))
//                )
//                .andExpect(status().isCreated())
//                .andDo(document("request-join-team",
//                                resourceDetails().tags("Team 생성")
//                                        .description("학교, 기업, 지역, 크루 공통. 불필요한 값은 null로 보내기."),
//                                DocumentConfig.getDocumentRequest(),
//                                DocumentConfig.getDocumentResponse(),
//                                requestHeaders(
//                                        headerWithName("Authorization").description("인증 토큰")
//                                ),
//                                requestFields(
//                                        fieldWithPath("teamType").description("팀 타입 : [SCHOOL/COMPANY/CREW/REGION]"),
//                                        fieldWithPath("teamId").description("team id")
//                                ),
//                                responseFields(
//                                        fieldWithPath("code").description("응답 코드"),
//                                        fieldWithPath("title").description("응답 코드 별 클라이언트 노출 제목"),
//                                        fieldWithPath("message").description("응답 코드 별 클라이언트 노출 메세지"),
//                                        fieldWithPath("data").description("데이터 없음")
//                                )
//                        )
//                );
//    }

    @Test
    @WithMockCustomUser
    @DisplayName("팀 검색")
    void searchTeam() throws Exception {
        TeamSearchResponse teamSearchResponse = TeamSearchResponse.builder()
                .teamId(1L)
                .teamType(TeamType.COMPANY)
                .teamName("WWOOSS")
                .address("주소")
                .build();
        TeamSearchListResponse teamSearchListResponse = TeamSearchListResponse.builder()
                .hasNext(false)
                .teamSearchList(List.of(teamSearchResponse))
                .build();
        given(teamService.searchTeam(any(), any(), any())).willReturn(teamSearchListResponse);

        this.mockMvc.perform(RestDocumentationRequestBuilders.get("/api/v1/team/team-type/{team-type}", "SCHOOL")
                        .with(csrf().asHeader())
                        .header("Authorization", "bearer TEST_ACCESS")
                        .contextPath("/api")
                        .param("size", "10")
                        .param("page", "0")
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andDo(document("search-team",
                                resourceDetails().tags("Team 검색")
                                        .description("팀별 팀이름 기반 검색"),
                                DocumentConfig.getDocumentRequest(),
                                DocumentConfig.getDocumentResponse(),
                                requestHeaders(
                                        headerWithName("Authorization").description("인증 토큰")
                                ),
                                pathParameters(
                                        parameterWithName("team-type").description("팀 타입 - [COMPANY/SCHOOL/CREW/REGION]")
                                ),
                                requestParameters(
                                        parameterWithName("page").description("현재 페이지"),
                                        parameterWithName("size").description("한 페이지 당 결과값").optional()
                                ),
                                responseFields(
                                        fieldWithPath("code").description("응답 코드"),
                                        fieldWithPath("title").description("응답 코드 별 클라이언트 노출 제목"),
                                        fieldWithPath("message").description("응답 코드 별 클라이언트 노출 메세지"),
                                        fieldWithPath("data.hasNext").description("다음 여부"),
                                        fieldWithPath("data.teamSearchList[].teamId").description("팀 아이디"),
                                        fieldWithPath("data.teamSearchList[].teamType").description("팀 타입 - [COMPANY/SCHOOL/CREW/REGION]"),
                                        fieldWithPath("data.teamSearchList[].teamName").description("팀 이름"),
                                        fieldWithPath("data.teamSearchList[].address").description("팀 주소")
                                )
                        )
                );
    }
}
