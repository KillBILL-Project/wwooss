package com.bigbro.wwooss.v1.api.auth;

import com.bigbro.wwooss.v1.annotation.TestController;
import com.bigbro.wwooss.v1.annotation.WithMockCustomUser;
import com.bigbro.wwooss.v1.config.DocumentConfig;
import com.bigbro.wwooss.v1.dto.request.alarm.AlarmRequest;
import com.bigbro.wwooss.v1.dto.request.auth.ResetPasswordRequest;
import com.bigbro.wwooss.v1.dto.response.alarm.AlarmResponse;
import com.bigbro.wwooss.v1.enumType.DayOfWeek;
import com.bigbro.wwooss.v1.service.auth.AuthService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

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
@WebMvcTest(AuthApi.class)
class AuthApiTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AuthService authService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    @WithMockCustomUser
    @DisplayName("회원탈퇴")
    void withdrawal() throws Exception {
        this.mockMvc.perform(RestDocumentationRequestBuilders.delete("/api/v1/auth/withdrawal")
                        .with(csrf().asHeader())
                        .contextPath("/api")
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andDo(document("withdrawal-user",
                                resourceDetails().tags("회원 탈퇴"),
                                DocumentConfig.getDocumentRequest(),
                                DocumentConfig.getDocumentResponse(),
                                requestHeaders(
                                        headerWithName("Authorization").description("인증 토큰")
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
    @DisplayName("임시비밀번호 발급")
    void resetPassword() throws Exception {
        ResetPasswordRequest resetPasswordRequest = ResetPasswordRequest.from("dddd@naver.com");

        this.mockMvc.perform(RestDocumentationRequestBuilders.patch("/api/v1/auth/reset-password")
                        .with(csrf().asHeader())
                        .contextPath("/api")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(resetPasswordRequest))
                )
                .andExpect(status().isOk())
                .andDo(document("reset-password",
                                resourceDetails().tags("임시 비밀번호 발급"),
                                DocumentConfig.getDocumentRequest(),
                                DocumentConfig.getDocumentResponse(),
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