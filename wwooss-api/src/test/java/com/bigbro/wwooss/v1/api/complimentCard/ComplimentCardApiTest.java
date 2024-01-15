package com.bigbro.wwooss.v1.api.complimentCard;


import static com.epages.restdocs.apispec.MockMvcRestDocumentationWrapper.document;
import static com.epages.restdocs.apispec.MockMvcRestDocumentationWrapper.resourceDetails;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.requestParameters;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.bigbro.wwooss.v1.annotation.TestController;
import com.bigbro.wwooss.v1.annotation.WithMockCustomUser;
import com.bigbro.wwooss.v1.config.DocumentConfig;
import com.bigbro.wwooss.v1.dto.response.complimentCard.ComplimentCardListResponse;
import com.bigbro.wwooss.v1.dto.response.complimentCard.ComplimentCardResponse;
import com.bigbro.wwooss.v1.enumType.CardType;
import com.bigbro.wwooss.v1.service.complimentCard.ComplimentCardService;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.test.web.servlet.MockMvc;

@TestController
@WebMvcTest(ComplimentCardApi.class)
class ComplimentCardApiTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ComplimentCardService complimentCardService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    @WithMockCustomUser
    @DisplayName("칭찬 카드 조회")
    void getComplimentCardList() throws Exception {
        List<ComplimentCardResponse> complimentCardResponses = List.of(ComplimentCardResponse.builder()
                .complimentCardId(1L)
                .title("칭찬 제목")
                .contents("칭찬 내용")
                .cardType(CardType.WEEKLY)
                .cardImage("image path")
                .build());
        ComplimentCardListResponse complimentCardListResponse = ComplimentCardListResponse.builder()
                .hasNext(false)
                .complimentCardResponses(complimentCardResponses)
                .build();

        given(this.complimentCardService.getComplimentCardList(any(), any(), any())).willReturn(complimentCardListResponse);

        this.mockMvc.perform(RestDocumentationRequestBuilders.get("/api/v1/compliment-card")
                        .with(csrf().asHeader())
                        .contextPath("/api")
                        .param("card-type", "WEEKLY")
                        .param("size", "10")
                        .param("page", "0")
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andDo(document("get-compliment-card",
                                resourceDetails().tags("칭찬 카드 조회"),
                                DocumentConfig.getDocumentRequest(),
                                DocumentConfig.getDocumentResponse(),
                                requestParameters(
                                        parameterWithName("card-type").description("칭찬 카드 조회 : "
                                                + "[WEEKLY/INTEGRATE]"),
                                        parameterWithName("page").description("현재 페이지"),
                                        parameterWithName("size").description("한 페이지 당 결과값").optional()
                                ),
                                responseFields(
                                        fieldWithPath("code").description("응답 코드"),
                                        fieldWithPath("title").description("응답 코드 별 클라이언트 노출 제목"),
                                        fieldWithPath("message").description("응답 코드 별 클라이언트 노출 메세지"),
                                        fieldWithPath("data.hasNext").description("다음 페이지 존재"),
                                        fieldWithPath("data.complimentCardResponses[].complimentCardId").description("칭찬 카드 ID"),
                                        fieldWithPath("data.complimentCardResponses[].title").description("칭찬 카드 "
                                                + "제목"),
                                        fieldWithPath("data.complimentCardResponses[].contents").description("칭찬 "
                                                + "카드 내용"),
                                        fieldWithPath("data.complimentCardResponses[].cardType").description("칭찬 "
                                                + "카드 타입 : [WEEKLY/INTEGRATE]"),
                                        fieldWithPath("data.complimentCardResponses[].cardImage").description("칭찬"
                                                + " 카드 이미지")
                                )
                        )
                );
    }

}