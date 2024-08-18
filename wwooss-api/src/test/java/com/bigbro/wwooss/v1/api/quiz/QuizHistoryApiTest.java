package com.bigbro.wwooss.v1.api.quiz;

import com.bigbro.wwooss.v1.annotation.TestController;
import com.bigbro.wwooss.v1.annotation.WithMockCustomUser;
import com.bigbro.wwooss.v1.config.DocumentConfig;
import com.bigbro.wwooss.v1.dto.option.OptionResponse;
import com.bigbro.wwooss.v1.dto.response.quiz.QuizResponse;
import com.bigbro.wwooss.v1.entity.quiz.Quiz;
import com.bigbro.wwooss.v1.enumType.QuizStatusType;
import com.bigbro.wwooss.v1.enumType.QuizType;
import com.bigbro.wwooss.v1.service.quiz.QuizService;
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
@WebMvcTest(QuizApi.class)
class QuizApiTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private QuizService quizService;

    @Test
    @WithMockCustomUser
    @DisplayName("퀴즈 조회")
    void getQuiz() throws Exception {

        // given
        Quiz quiz = Quiz.builder()
                .quizId(1L)
                .question("어떤 성분으로 이루어졌는가?")
                .type(QuizType.MULTI_CHOICE)
                .build();
        List<OptionResponse> options = List.of(OptionResponse.builder()
                .value("플라스틱")
                .optionId(1L)
                .build(),
                OptionResponse.builder()
                .value("종이")
                .optionId(2L)
                .build());

        given(this.quizService.findQuiz(any())).willReturn(QuizResponse.of(quiz, 45L, "image", options));

        this.mockMvc.perform(RestDocumentationRequestBuilders.get("/api/v1/quiz/{quiz-id}", 1L)
                        .with(csrf().asHeader())
                        .header("Authorization", "bearer TEST_ACCESS")
                        .contextPath("/api")
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andDo(document("get-quiz",
                                resourceDetails().tags("Quiz 조회"),
                                DocumentConfig.getDocumentRequest(),
                                DocumentConfig.getDocumentResponse(),
                                requestHeaders(
                                        headerWithName("Authorization").description("인증 토큰")
                                ),
                                responseFields(
                                        fieldWithPath("code").description("응답 코드"),
                                        fieldWithPath("title").description("응답 코드 별 클라이언트 노출 제목"),
                                        fieldWithPath("message").description("응답 코드 별 클라이언트 노출 메세지"),
                                        fieldWithPath("data.quizId").description("Quiz ID"),
                                        fieldWithPath("data.question").description("Quiz 질문"),
                                        fieldWithPath("data.correctRate").description("Quiz 정답률"),
                                        fieldWithPath("data.quizType").description("Quiz 타입 - [MULTI_CHOICE - 객관식 : 선택지 배열 리턴], [OX :  O,X 배열 리턴], [SHORT_ANSWER - 객관식 : 빈배열리턴]"),
                                        fieldWithPath("data.imagePath").description("퀴즈 이미지"),
                                        fieldWithPath("data.options[].optionId").description("선택지 ID"),
                                        fieldWithPath("data.options[].value").description("선택지 값")
                                )
                        )
                );
    }

    @Test
    @WithMockCustomUser
    @DisplayName("퀴즈 풀기")
    void solveQuiz() throws Exception {
        // given
        given(this.quizService.solveQuiz(any(), any(), any())).willReturn(QuizStatusType.PASS);

        this.mockMvc.perform(RestDocumentationRequestBuilders.patch("/api/v1/quiz/{quiz-id}", 1L)
                        .with(csrf().asHeader())
                        .header("Authorization", "bearer TEST_ACCESS")
                        .contextPath("/api")
                        .param("answer", "1")
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andDo(document("solve-quiz",
                                resourceDetails().tags("Quiz 풀기"),
                                DocumentConfig.getDocumentRequest(),
                                DocumentConfig.getDocumentResponse(),
                                requestHeaders(
                                        headerWithName("Authorization").description("인증 토큰")
                                ),
                                responseFields(
                                        fieldWithPath("code").description("응답 코드"),
                                        fieldWithPath("title").description("응답 코드 별 클라이언트 노출 제목"),
                                        fieldWithPath("message").description("응답 코드 별 클라이언트 노출 메세지"),
                                        fieldWithPath("data").description("Result - PASS/FAIL")
                                )
                        )
                );
    }
}
