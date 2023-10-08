package com.bigbro.wwooss.v1.api.trash.can;

import com.bigbro.wwooss.v1.annotation.TestController;
import com.bigbro.wwooss.v1.config.DocumentConfig;
import com.bigbro.wwooss.v1.domain.request.trash.can.TrashCanContentsRequest;
import com.bigbro.wwooss.v1.service.trash.can.TrashCanContentsService;
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
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@TestController
@WebMvcTest(TrashCanContentsApi.class)
class TrashCanContentsApiTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TrashCanContentsService trashCanContentsService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    @DisplayName("쓰레기통 내용물 적립")
    void createTrashCanContents() throws Exception {
        TrashCanContentsRequest trashCanContentsRequest = TrashCanContentsRequest.builder()
                .trashCount(1L)
                .size(1)
                .trashInfoId(1L)
                .build();

        this.mockMvc.perform(RestDocumentationRequestBuilders.post("/api/v1/trash-can-contents")
                        .contextPath("/api")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(trashCanContentsRequest))
                )
                .andExpect(status().isCreated())
                .andDo(document("create-trash-can-contents",
                                resourceDetails().tags("쓰레기통 내용물 생성"),
                                DocumentConfig.getDocumentRequest(),
                                DocumentConfig.getDocumentResponse(),
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