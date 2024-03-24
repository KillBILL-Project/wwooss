package com.bigbro.wwooss.v1.api.trash.can;

import com.bigbro.wwooss.v1.annotation.TestController;
import com.bigbro.wwooss.v1.annotation.WithMockCustomUser;
import com.bigbro.wwooss.v1.config.DocumentConfig;
import com.bigbro.wwooss.v1.dto.CarbonSavingByTrashCategory;
import com.bigbro.wwooss.v1.dto.RefundByTrashCategory;
import com.bigbro.wwooss.v1.dto.request.trash.can.TrashCanContentsRequest;
import com.bigbro.wwooss.v1.dto.response.trash.EmptyTrashResultResponse;
import com.bigbro.wwooss.v1.enumType.TrashType;
import com.bigbro.wwooss.v1.service.complimentCard.ComplimentCardService;
import com.bigbro.wwooss.v1.service.complimentCard.ComplimentConditionLogService;
import com.bigbro.wwooss.v1.service.notification.NotificationService;
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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
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

    @MockBean
    private ComplimentCardService complimentCardService;

    @MockBean
    private ComplimentConditionLogService complimentConditionLogService;;

    @MockBean
    private NotificationService notificationService;;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    @WithMockCustomUser
    @DisplayName("쓰레기통 내용물 양 가져오기")
    void getTrashCanContentsCount() throws Exception {
        given(this.trashCanContentsService.getTrashCanContentsCount(any())).willReturn(20L);

        this.mockMvc.perform(RestDocumentationRequestBuilders.get("/api/v1/trash-can-contents/total-count")
                        .with(csrf().asHeader())
                        .header("Authorization", "bearer TEST_ACCESS")
                        .contextPath("/api")
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andDo(document("get-trash-can-contents-count",
                                resourceDetails().tags("쓰레기통 내용물 갯수 가져오기"),
                                DocumentConfig.getDocumentRequest(),
                                DocumentConfig.getDocumentResponse(),
                                requestHeaders(
                                        headerWithName("Authorization").description("인증 토큰")
                                ),
                                responseFields(
                                        fieldWithPath("code").description("응답 코드"),
                                        fieldWithPath("title").description("응답 코드 별 클라이언트 노출 제목"),
                                        fieldWithPath("message").description("응답 코드 별 클라이언트 노출 메세지"),
                                        fieldWithPath("data").description("쓰레기통 내용물 갯수")
                                )
                        )
                );
    }

    @Test
    @WithMockCustomUser
    @DisplayName("쓰레기통 내용물 적립")
    void createTrashCanContents() throws Exception {
        TrashCanContentsRequest trashCanContentsRequest = TrashCanContentsRequest.builder()
                .trashInfoId(1L)
                .build();

        this.mockMvc.perform(RestDocumentationRequestBuilders.post("/api/v1/trash-can-contents")
                        .with(csrf().asHeader())
                        .header("Authorization", "bearer TEST_ACCESS")
                        .contextPath("/api")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(trashCanContentsRequest))
                )
                .andExpect(status().isCreated())
                .andDo(document("create-trash-can-contents",
                                resourceDetails().tags("쓰레기 버리기"),
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
    @DisplayName("쓰레기통 비우기")
    void deleteTrashCanContents() throws Exception {
        List<CarbonSavingByTrashCategory> carbonSavingByTrashCategory = List.of(CarbonSavingByTrashCategory.of(TrashType.CAN, 10.0D));
        List<RefundByTrashCategory> refundByTrashCategory = List.of(RefundByTrashCategory.of(TrashType.CAN, 100L));
        EmptyTrashResultResponse emptyTrashResultResponse = EmptyTrashResultResponse.of(
                30.0D, carbonSavingByTrashCategory, 100L, refundByTrashCategory, 1L);

        given(this.trashCanContentsService.deleteTrashCanContents(1L)).willReturn(emptyTrashResultResponse);

        this.mockMvc.perform(RestDocumentationRequestBuilders.delete("/api/v1/trash-can-contents")
                        .with(csrf().asHeader())
                        .header("Authorization", "bearer TEST_ACCESS")
                        .contextPath("/api")
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andDo(document("delete-trash-can-contents",
                                resourceDetails().tags("쓰레기통 비우기"),
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
