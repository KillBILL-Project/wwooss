package com.bigbro.wwooss.v1.api.quizHistory;

import com.bigbro.wwooss.v1.annotation.TestController;
import com.bigbro.wwooss.v1.annotation.WithMockCustomUser;
import com.bigbro.wwooss.v1.config.DocumentConfig;
import com.bigbro.wwooss.v1.dto.response.quizHistory.QuizHistoryInfo;
import com.bigbro.wwooss.v1.dto.response.quizHistory.QuizHistoryListResponse;
import com.bigbro.wwooss.v1.entity.quiz.Quiz;
import com.bigbro.wwooss.v1.entity.quiz.QuizHistory;
import com.bigbro.wwooss.v1.enumType.QuizStatusType;
import com.bigbro.wwooss.v1.repository.user.UserRepository;
import com.bigbro.wwooss.v1.service.quizHistory.QuizHistoryService;
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
@WebMvcTest(QuizHistoryApi.class)
class QuizHistoryApiTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private QuizHistoryService quizHistoryService;

    @MockBean
    private UserRepository userRepository;

    @Test
    @WithMockCustomUser
    @DisplayName("퀴즈 목록 조회")
    void getQuizHistoryList() throws Exception {

        // given
        Quiz quiz = Quiz.builder()
                .quizId(1L)
                .tokenValue(1.0D)
                .build();
        QuizHistory quizHistory = QuizHistory.builder()
                .quizHistoryId(1L)
                .quiz(quiz)
                .status(QuizStatusType.PASS)
                .createdAt(LocalDateTime.now())
                .build();
        QuizHistoryInfo quizHistoryInfo = QuizHistoryInfo.of(quizHistory);
        QuizHistoryListResponse quizHistoryListResponse = QuizHistoryListResponse.of(false, List.of(quizHistoryInfo));
        given(this.quizHistoryService.getQuizHistoryList(any(), any())).willReturn(quizHistoryListResponse);

        this.mockMvc.perform(RestDocumentationRequestBuilders.get("/api/v1/quiz-history")
                        .with(csrf().asHeader())
                        .header("Authorization", "bearer TEST_ACCESS")
                        .contextPath("/api")
                        .param("size", "10")
                        .param("page", "0")
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andDo(document("get-quiz-history",
                                resourceDetails().tags("Quiz 목록 조회"),
                                DocumentConfig.getDocumentRequest(),
                                DocumentConfig.getDocumentResponse(),
                                requestParameters(
                                        parameterWithName("page").description("현재 페이지").optional(),
                                        parameterWithName("size").description("한 페이지 당 결과값").optional()
                                ),
                                requestHeaders(
                                        headerWithName("Authorization").description("인증 토큰")
                                ),
                                responseFields(
                                        fieldWithPath("code").description("응답 코드"),
                                        fieldWithPath("title").description("응답 코드 별 클라이언트 노출 제목"),
                                        fieldWithPath("message").description("응답 코드 별 클라이언트 노출 메세지"),
                                        fieldWithPath("data.hasNext").description("다음 페이지 여부"),
                                        fieldWithPath("data.quizHistoryInfoList[].quizHistoryId").description("Quiz History ID"),
                                        fieldWithPath("data.quizHistoryInfoList[].quizId").description("Quiz ID"),
                                        fieldWithPath("data.quizHistoryInfoList[].status").description("Quiz Status - [UNSOLVED - 안푼문제], [PASS - 정답], [FAIL - 실패], [ISSUED - 토큰 발급 완료]]"),
                                        fieldWithPath("data.quizHistoryInfoList[].tokenValue").description("토큰 값"),
                                        fieldWithPath("data.quizHistoryInfoList[].createdAt").description("생성 날짜")
                                )
                        )
                );
    }
}
