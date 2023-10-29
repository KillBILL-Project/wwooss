package com.bigbro.wwooss.v1.api.trash.can;

import com.bigbro.wwooss.v1.annotation.TestController;
import com.bigbro.wwooss.v1.config.DocumentConfig;
import com.bigbro.wwooss.v1.dto.response.trash.TrashCanHistoryListResponse;
import com.bigbro.wwooss.v1.dto.response.trash.TrashCanHistoryResponse;
import com.bigbro.wwooss.v1.service.trash.can.TrashCanHistoryService;
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
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.requestParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@TestController
@WebMvcTest(TrashCanHistoryApi.class)
class TrashCanHistoryApiTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TrashCanHistoryService trashCanHistoryService;

    @Test
    @DisplayName("쓰레기통 목록 조회")
    void findTrashCanHistories() throws Exception {
        LocalDateTime now = LocalDateTime.now();
        List<TrashCanHistoryResponse> trashCanHistoryResponseList = List.of(TrashCanHistoryResponse.builder()
                .trashCanHistoryId(1L)
                .createdAt(now)
                .build());
        TrashCanHistoryListResponse trashCanHistoryListResponse = TrashCanHistoryListResponse.builder()
                .hasNext(false)
                .trashCanHistoryResponseList(trashCanHistoryResponseList)
                .build();

        given(this.trashCanHistoryService.findTrashCanHistoryList(any(), any(), any())).willReturn(trashCanHistoryListResponse);

        this.mockMvc.perform(RestDocumentationRequestBuilders.get("/api/v1/trash-can-histories")
                        .contextPath("/api")
                        .param("date", "2023-10")
                        .param("size", "10")
                        .param("page", "0")
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andDo(document("get-trash-can-histories",
                                resourceDetails().tags("쓰레기통 목록 조회"),
                                DocumentConfig.getDocumentRequest(),
                                DocumentConfig.getDocumentResponse(),
                                requestParameters(
                                        parameterWithName("date").description("조회 날짜 : [null / YYYY-MM / YYYY]").optional(),
                                        parameterWithName("page").description("현재 페이지"),
                                        parameterWithName("size").description("한 페이지 당 결과값").optional()
                                ),
                                responseFields(
                                        fieldWithPath("code").description("응답 코드"),
                                        fieldWithPath("title").description("응답 코드 별 클라이언트 노출 제목"),
                                        fieldWithPath("message").description("응답 코드 별 클라이언트 노출 메세지"),
                                        fieldWithPath("data.hasNext").description("다음 페이지 존재"),
                                        fieldWithPath("data.trashCanHistoryResponseList[].trashCanHistoryId").description("쓰레기통 비우기 히스토리 ID"),
                                        fieldWithPath("data.trashCanHistoryResponseList[].createdAt").description("비우기 생성 일자")
                                )
                        )
                );
    }
}
