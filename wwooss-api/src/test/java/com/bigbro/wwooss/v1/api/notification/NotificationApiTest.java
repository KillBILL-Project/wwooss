package com.bigbro.wwooss.v1.api.notification;

import com.bigbro.wwooss.v1.annotation.TestController;
import com.bigbro.wwooss.v1.annotation.WithMockCustomUser;
import com.bigbro.wwooss.v1.config.DocumentConfig;
import com.bigbro.wwooss.v1.dto.response.notification.NotificationListResponse;
import com.bigbro.wwooss.v1.dto.response.notification.NotificationResponse;
import com.bigbro.wwooss.v1.repository.user.UserRepository;
import com.bigbro.wwooss.v1.service.notification.NotificationService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;

import static com.epages.restdocs.apispec.MockMvcRestDocumentationWrapper.document;
import static com.epages.restdocs.apispec.MockMvcRestDocumentationWrapper.resourceDetails;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.requestParameters;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@TestController
@WebMvcTest(NotificationApi.class)
class NotificationApiTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private NotificationService notificationService;

    @MockBean
    private UserRepository userRepository;

    @Test
    @WithMockCustomUser
    @DisplayName("알림 목록 조회")
    void getNotificationList() throws Exception {
        List<NotificationResponse> notificationResponses = List.of(NotificationResponse.builder()
                .notificationId(1L)
                .title("알림 제목")
                .message("알림 내용")
                .deepLink("deep link")
                .createdAt(LocalDateTime.now())
                .build());
        NotificationListResponse notificationListResponse = NotificationListResponse.of(false, notificationResponses);

        given(this.notificationService.getNotificationList(any(), any())).willReturn(notificationListResponse);

        this.mockMvc.perform(RestDocumentationRequestBuilders.get("/api/v1/notification")
                        .with(csrf().asHeader())
                        .header("Authorization", "bearer TEST_ACCESS")
                        .contextPath("/api")
                        .param("size", "10")
                        .param("page", "0")
                        .param("direction", "DESC")
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andDo(document("get-notification",
                                resourceDetails().tags("알림 목록 조회"),
                                DocumentConfig.getDocumentRequest(),
                                DocumentConfig.getDocumentResponse(),
                                requestParameters(
                                        parameterWithName("page").description("현재 페이지").optional(),
                                        parameterWithName("size").description("한 페이지 당 결과값").optional(),
                                        parameterWithName("direction").description("정렬 방향 - [DESC/ASC]").optional()
                                ),
                                requestHeaders(
                                        headerWithName("Authorization").description("인증 토큰")
                                ),
                                responseFields(
                                        fieldWithPath("code").description("응답 코드"),
                                        fieldWithPath("title").description("응답 코드 별 클라이언트 노출 제목"),
                                        fieldWithPath("message").description("응답 코드 별 클라이언트 노출 메세지"),
                                        fieldWithPath("data.hasNext").description("다음 페이지 존재"),
                                        fieldWithPath("data.notificationResponses[].notificationId").description("알림 ID"),
                                        fieldWithPath("data.notificationResponses[].title").description("알림 제목"),
                                        fieldWithPath("data.notificationResponses[].message").description("알림 내용"),
                                        fieldWithPath("data.notificationResponses[].deepLink").description("딥링크 추후 필요할 수도 있움. 지금 사용X"),
                                        fieldWithPath("data.notificationResponses[].createdAt").description("알림 생성 날짜")
                                )
                        )
                );
    }
}
