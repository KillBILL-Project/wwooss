package com.bigbro.wwooss.v1.api.team;

import com.bigbro.wwooss.v1.annotation.TestController;
import com.bigbro.wwooss.v1.annotation.WithMockCustomUser;
import com.bigbro.wwooss.v1.config.DocumentConfig;
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

import static com.epages.restdocs.apispec.MockMvcRestDocumentationWrapper.document;
import static com.epages.restdocs.apispec.MockMvcRestDocumentationWrapper.resourceDetails;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.partWithName;
import static org.springframework.restdocs.request.RequestDocumentation.requestParts;
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
    @DisplayName("알람 생성")
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
}
