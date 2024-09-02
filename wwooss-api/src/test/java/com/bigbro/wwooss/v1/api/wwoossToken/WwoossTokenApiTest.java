package com.bigbro.wwooss.v1.api.wwoossToken;

import com.bigbro.wwooss.v1.annotation.TestController;
import com.bigbro.wwooss.v1.annotation.WithMockCustomUser;
import com.bigbro.wwooss.v1.config.DocumentConfig;
import com.bigbro.wwooss.v1.dto.request.wwoossToken.IssueTokenRequest;
import com.bigbro.wwooss.v1.dto.response.wwoossToken.IssuedWwoossToken;
import com.bigbro.wwooss.v1.enumType.IssueType;
import com.bigbro.wwooss.v1.service.wwoossToken.WwoossTokenService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.MockMvc;

import static com.epages.restdocs.apispec.MockMvcRestDocumentationWrapper.document;
import static com.epages.restdocs.apispec.MockMvcRestDocumentationWrapper.resourceDetails;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.requestParameters;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@TestController
@WebMvcTest(WwoossTokenApi.class)
class WwoossTokenApiTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private WwoossTokenService wwoossTokenService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    @WithMockCustomUser
    @DisplayName("Wwooss Token 발급")
    void issueToken() throws Exception {

        IssueTokenRequest issueTokenRequest = IssueTokenRequest.builder()
                .issueType(IssueType.QUIZ)
                .build();
        IssuedWwoossToken issuedWwoossToken = IssuedWwoossToken.builder()
                .wwoossTokenId(1L)
                .issueType(IssueType.QUIZ)
                .value(10D)
                .build();
        given(this.wwoossTokenService.issueWwoossToken(any(), any())).willReturn(issuedWwoossToken);

        this.mockMvc.perform(RestDocumentationRequestBuilders.post("/api/v1/wwooss-token")
                        .with(csrf().asHeader())
                        .header("Authorization", "bearer TEST_ACCESS")
                        .contextPath("/api")
                        .content(objectMapper.writeValueAsString(issueTokenRequest))
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isCreated())
                .andDo(document("issue-wwooss-token",
                                resourceDetails().tags("Wwooss Token 발급"),
                                DocumentConfig.getDocumentRequest(),
                                DocumentConfig.getDocumentResponse(),
                                requestFields(
                                        fieldWithPath("issueType").type(JsonFieldType.STRING).description("QUIZ")
                                ),
                                requestHeaders(
                                        headerWithName("Authorization").description("인증 토큰")
                                ),
                                responseFields(
                                        fieldWithPath("code").description("응답 코드"),
                                        fieldWithPath("title").description("응답 코드 별 클라이언트 노출 제목"),
                                        fieldWithPath("message").description("응답 코드 별 클라이언트 노출 메세지"),
                                        fieldWithPath("data.wwoossTokenId").description("발급된 토크 ID"),
                                        fieldWithPath("data.issueType").description("발급된 토큰 유형"),
                                        fieldWithPath("data.value").description("발급된 토큰 값")
                                )
                        )
                );
    }
}
