package com.bigbro.wwooss.v1.api.trash.can;

import com.bigbro.wwooss.v1.annotation.TestController;
import com.bigbro.wwooss.v1.job.DocumentConfig;
import com.bigbro.wwooss.v1.dto.CarbonEmissionByTrashCategory;
import com.bigbro.wwooss.v1.dto.RefundByTrashCategory;
import com.bigbro.wwooss.v1.dto.request.trash.can.TrashCanContentsRequest;
import com.bigbro.wwooss.v1.dto.response.trash.EmptyTrashResultResponse;
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

import java.util.List;

import static com.epages.restdocs.apispec.MockMvcRestDocumentationWrapper.document;
import static com.epages.restdocs.apispec.MockMvcRestDocumentationWrapper.resourceDetails;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;

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
                        .with(csrf().asHeader())
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

    @Test
    @DisplayName("쓰레기통 비우기")
    void deleteTrashCanContents() throws Exception {
        List<CarbonEmissionByTrashCategory> carbonEmissionByTrashCategory = List.of(CarbonEmissionByTrashCategory.of("플라스틱", 10.0D));
        List<RefundByTrashCategory> refundByTrashCategory = List.of(RefundByTrashCategory.of("플라스틱", 100L));
        EmptyTrashResultResponse emptyTrashResultResponse = EmptyTrashResultResponse.of(
                30.0D, carbonEmissionByTrashCategory, 100L, refundByTrashCategory);

        given(this.trashCanContentsService.deleteTrashCanContents(1L)).willReturn(emptyTrashResultResponse);

        this.mockMvc.perform(RestDocumentationRequestBuilders.delete("/api/v1/trash-can-contents")
                        .with(csrf().asHeader())
                        .contextPath("/api")
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andDo(document("delete-trash-can-contents",
                                resourceDetails().tags("쓰레기통 비우기"),
                                DocumentConfig.getDocumentRequest(),
                                DocumentConfig.getDocumentResponse(),
                                responseFields(
                                        fieldWithPath("code").description("응답 코드"),
                                        fieldWithPath("title").description("응답 코드 별 클라이언트 노출 제목"),
                                        fieldWithPath("message").description("응답 코드 별 클라이언트 노출 메세지"),
                                        fieldWithPath("data.totalCarbonEmission").description("비운 총 탄소 배출량 "),
                                        fieldWithPath("data.carbonEmissionByTrashCategoryList[].trashCategoryName").description("쓰레기 종류"),
                                        fieldWithPath("data.carbonEmissionByTrashCategoryList[].carbonEmission").description("쓰레기 종류별 총 탄소 배출량"),
                                        fieldWithPath("data.totalRefund").description("비운 총 환급금"),
                                        fieldWithPath("data.refundByTrashCategoryList[].trashCategoryName").description("쓰레기 종류"),
                                        fieldWithPath("data.refundByTrashCategoryList[].refund").description("쓰레기 종류별 총 환급금")
                                )
                        )
                );
    }
}