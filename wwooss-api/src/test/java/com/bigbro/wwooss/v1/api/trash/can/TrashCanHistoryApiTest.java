package com.bigbro.wwooss.v1.api.trash.can;

import com.bigbro.wwooss.v1.annotation.TestController;
import com.bigbro.wwooss.v1.annotation.WithMockCustomUser;
import com.bigbro.wwooss.v1.config.DocumentConfig;
import com.bigbro.wwooss.v1.dto.CarbonSavingByTrashCategory;
import com.bigbro.wwooss.v1.dto.RefundByTrashCategory;
import com.bigbro.wwooss.v1.dto.response.trash.EmptyTrashResultResponse;
import com.bigbro.wwooss.v1.dto.response.trash.TrashCanHistoryListResponse;
import com.bigbro.wwooss.v1.dto.response.trash.TrashCanHistoryResponse;
import com.bigbro.wwooss.v1.enumType.TrashType;
import com.bigbro.wwooss.v1.service.complimentCard.ComplimentCardService;
import com.bigbro.wwooss.v1.service.complimentCard.ComplimentConditionLogService;
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
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.requestParameters;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@TestController
@WebMvcTest(TrashCanHistoryApi.class)
class TrashCanHistoryApiTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TrashCanHistoryService trashCanHistoryService;

    @MockBean
    private ComplimentCardService complimentCardService;

    @MockBean
    private ComplimentConditionLogService complimentConditionLogService;

    @Test
    @WithMockCustomUser
    @DisplayName("쓰레기통 히스토리 조회")
    void findTrashCanHistories() throws Exception {
        LocalDateTime now = LocalDateTime.now();
        List<TrashCanHistoryResponse> trashCanHistoryResponseList = List.of(TrashCanHistoryResponse.builder()
                .trashCanHistoryId(1L)
                .carbonSaving(1D)
                .refund(1L)
                .createdAt(now)
                .build());
        TrashCanHistoryListResponse trashCanHistoryListResponse = TrashCanHistoryListResponse.builder()
                .hasNext(false)
                .trashCanHistoryResponseList(trashCanHistoryResponseList)
                .build();

        given(this.trashCanHistoryService.findTrashCanHistoryList(any(), any(), any())).willReturn(trashCanHistoryListResponse);

        this.mockMvc.perform(RestDocumentationRequestBuilders.get("/api/v1/trash-can-histories")
                        .with(csrf().asHeader())
                        .contextPath("/api")
                        .header("Authorization", "bearer TEST_ACCESS")
                        .param("date", "2023-10")
                        .param("size", "10")
                        .param("page", "0")
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andDo(document("get-trash-can-histories",
                                resourceDetails().tags("쓰레기통 비우기 이력 조회"),
                                DocumentConfig.getDocumentRequest(),
                                DocumentConfig.getDocumentResponse(),
                                requestParameters(
                                        parameterWithName("date").description("조회 날짜 : [null / YYYY-MM / YYYY]").optional(),
                                        parameterWithName("page").description("현재 페이지"),
                                        parameterWithName("size").description("한 페이지 당 결과값").optional()
                                ),
                                requestHeaders(
                                        headerWithName("Authorization").description("인증 토큰")
                                ),
                                responseFields(
                                        fieldWithPath("code").description("응답 코드"),
                                        fieldWithPath("title").description("응답 코드 별 클라이언트 노출 제목"),
                                        fieldWithPath("message").description("응답 코드 별 클라이언트 노출 메세지"),
                                        fieldWithPath("data.hasNext").description("다음 페이지 존재"),
                                        fieldWithPath("data.trashCanHistoryResponseList[].trashCanHistoryId").description("쓰레기통 비우기 히스토리 ID"),
                                        fieldWithPath("data.trashCanHistoryResponseList[].carbonSaving").description("탄소 절감량"),
                                        fieldWithPath("data.trashCanHistoryResponseList[].refund").description("환급금"),
                                        fieldWithPath("data.trashCanHistoryResponseList[].createdAt").description("비우기 생성 일자")
                                )
                        )
                );
    }

    @Test
    @WithMockCustomUser
    @DisplayName("히스토리 상세 조회")
    void findTrashCanHistoryDetail() throws Exception {
        List<CarbonSavingByTrashCategory> carbonSavingByTrashCategory = List.of(CarbonSavingByTrashCategory.of(TrashType.CAN, 10.0D));
        List<RefundByTrashCategory> refundByTrashCategory = List.of(RefundByTrashCategory.of(TrashType.CAN, 100L));
        EmptyTrashResultResponse emptyTrashResultResponse = EmptyTrashResultResponse.of(
                30.0D, carbonSavingByTrashCategory, 100L, refundByTrashCategory, 1L);

        given(this.trashCanHistoryService.findTrashCanHistoryDetail(any(), any())).willReturn(emptyTrashResultResponse);

        this.mockMvc.perform(RestDocumentationRequestBuilders.get("/api/v1/trash-can-histories/{trash-can-history-id}", 1L)
                        .with(csrf().asHeader())
                        .contextPath("/api")
                        .header("Authorization", "bearer TEST_ACCESS")
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andDo(document("get-trash-can-history-detail",
                                resourceDetails().tags("쓰레기통 비우기 상세 조회(히스토리 상세)"),
                                DocumentConfig.getDocumentRequest(),
                                DocumentConfig.getDocumentResponse(),
                                requestHeaders(
                                        headerWithName("Authorization").description("인증 토큰")
                                ),
                                responseFields(
                                        fieldWithPath("code").description("응답 코드"),
                                        fieldWithPath("title").description("응답 코드 별 클라이언트 노출 제목"),
                                        fieldWithPath("message").description("응답 코드 별 클라이언트 노출 메세지"),
                                        fieldWithPath("data.trashCanHistoryId").description("쓰레기 히스토리 ID"),
                                        fieldWithPath("data.totalCarbonSaving").description("비운 총 탄소 절감량 [gCO2]"),
                                        fieldWithPath("data.carbonSavingByTrashCategoryList[].trashCategoryName").description("쓰레기 종류"),
                                        fieldWithPath("data.carbonSavingByTrashCategoryList[].carbonSaving").description("쓰레기 종류별 총 탄소 절감량 [gCO2]"),
                                        fieldWithPath("data.totalRefund").description("비운 총 환급금 [원]"),
                                        fieldWithPath("data.refundByTrashCategoryList[].trashCategoryName").description("쓰레기 종류"),
                                        fieldWithPath("data.refundByTrashCategoryList[].refund").description("쓰레기 종류별 총 환급금 [원]")
                                )
                        )
                );
    }


}
