package com.bigbro.wwooss.v1.api.userActiveLog;

import com.bigbro.wwooss.v1.annotation.TestController;
import com.bigbro.wwooss.v1.annotation.WithMockCustomUser;
import com.bigbro.wwooss.v1.config.DocumentConfig;
import com.bigbro.wwooss.v1.dto.request.userActiveLog.RecordLogRequest;
import com.bigbro.wwooss.v1.dto.response.quiz.QuizResponse;
import com.bigbro.wwooss.v1.dto.response.userActiveLog.UserActiveLogResponse;
import com.bigbro.wwooss.v1.entity.userActiveLog.UserActiveLog;
import com.bigbro.wwooss.v1.enumType.ActiveLogType;
import com.bigbro.wwooss.v1.service.userActiveLog.UserActiveLogService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.test.web.servlet.MockMvc;

import static com.epages.restdocs.apispec.MockMvcRestDocumentationWrapper.document;
import static com.epages.restdocs.apispec.MockMvcRestDocumentationWrapper.resourceDetails;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@TestController
@WebMvcTest(UserActiveLogApi.class)
class UserActiveLogApiTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserActiveLogService userActiveLogService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    @WithMockCustomUser
    @DisplayName("유저 활동 기록")
    void recordLog() throws Exception {

        RecordLogRequest recordLogRequest = RecordLogRequest.builder()
                .activeLogType(ActiveLogType.QUIZ)
                .build();
        given(this.userActiveLogService.recordActiveLog(any(),any())).willReturn(UserActiveLogResponse.from(UserActiveLog.builder()
                .userActiveLogId(1L)
                .value(100D)
                .contents("퀴즈 활동")
                .build()));

        this.mockMvc.perform(RestDocumentationRequestBuilders.post("/api/v1/user-active-log")
                        .with(csrf().asHeader())
                        .header("Authorization", "bearer TEST_ACCESS")
                        .contextPath("/api")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(recordLogRequest))
                )
                .andExpect(status().isCreated())
                .andDo(document("logging-user-active",
                                resourceDetails().tags("유저 활동 로그 기록"),
                                DocumentConfig.getDocumentRequest(),
                                DocumentConfig.getDocumentResponse(),
                                requestHeaders(
                                        headerWithName("Authorization").description("인증 토큰")
                                ),
                                responseFields(
                                        fieldWithPath("code").description("응답 코드"),
                                        fieldWithPath("title").description("응답 코드 별 클라이언트 노출 제목"),
                                        fieldWithPath("message").description("응답 코드 별 클라이언트 노출 메세지"),
                                        fieldWithPath("data.userActiveLogId").description("유저 활동 로그 ID"),
                                        fieldWithPath("data.value").description("활동 value"),
                                        fieldWithPath("data.contents").description("활동 내용")
                                )
                        )
                );
    }

    @Test
    @WithMockCustomUser
    @DisplayName("유저 활동 인증")
    void verifiedLog() throws Exception {

        this.mockMvc.perform(RestDocumentationRequestBuilders.patch("/api/v1/user-active-log/{user-active-log-id}", 1L)
                        .with(csrf().asHeader())
                        .header("Authorization", "bearer TEST_ACCESS")
                        .contextPath("/api")
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andDo(document("verify-user-active-log",
                                resourceDetails().tags("유저 활동 로그 인증"),
                                DocumentConfig.getDocumentRequest(),
                                DocumentConfig.getDocumentResponse(),
                                requestHeaders(
                                        headerWithName("Authorization").description("인증 토큰")
                                ),
                                responseFields(
                                        fieldWithPath("code").description("응답 코드"),
                                        fieldWithPath("title").description("응답 코드 별 클라이언트 노출 제목"),
                                        fieldWithPath("message").description("응답 코드 별 클라이언트 노출 메세지"),
                                        fieldWithPath("data").description("응답 데이터 없음")
                                )
                        )
                );
    }
}
