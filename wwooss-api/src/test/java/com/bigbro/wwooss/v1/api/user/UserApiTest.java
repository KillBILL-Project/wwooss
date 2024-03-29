package com.bigbro.wwooss.v1.api.user;

import com.bigbro.wwooss.v1.annotation.TestController;
import com.bigbro.wwooss.v1.annotation.WithMockCustomUser;
import com.bigbro.wwooss.v1.config.DocumentConfig;
import com.bigbro.wwooss.v1.dto.request.user.UpdateFcmTokenRequest;
import com.bigbro.wwooss.v1.dto.request.user.UpdatePushConsentRequest;
import com.bigbro.wwooss.v1.service.user.UserService;
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
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@TestController
@WebMvcTest(UserApi.class)
class UserApiTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    @WithMockCustomUser
    @DisplayName("푸쉬 동의 여부 변경")
    void updatePushConsent() throws Exception {
        UpdatePushConsentRequest pushConsentRequest = UpdatePushConsentRequest.builder()
                .pushConsent(true)
                .build();

        this.mockMvc.perform(RestDocumentationRequestBuilders.patch("/api/v1/user/push-consent")
                        .with(csrf().asHeader())
                        .header("Authorization", "bearer TEST_ACCESS")
                        .contextPath("/api")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(pushConsentRequest))
                )
                .andExpect(status().isOk())
                .andDo(document("update-push-consent",
                                resourceDetails().tags("유저 푸쉬 알림 여부 상태 변경"),
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

    @Test
    @WithMockCustomUser
    @DisplayName("fcm 토큰 변경")
    void updateFcmToken() throws Exception {
        UpdateFcmTokenRequest updaetFcmRequest = UpdateFcmTokenRequest.builder()
                .fcmToken("FCM TOKEN")
                .build();

        this.mockMvc.perform(RestDocumentationRequestBuilders.patch("/api/v1/user/fcm-token")
                        .with(csrf().asHeader())
                        .header("Authorization", "bearer TEST_ACCESS")
                        .contextPath("/api")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updaetFcmRequest))
                )
                .andExpect(status().isOk())
                .andDo(document("update-fcm-token",
                                resourceDetails().tags("유저 푸쉬 알림 여부 상태 변경"),
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
