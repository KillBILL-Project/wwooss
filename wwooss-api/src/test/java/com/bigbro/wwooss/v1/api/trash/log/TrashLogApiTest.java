package com.bigbro.wwooss.v1.api.trash.log;

import com.bigbro.wwooss.v1.annotation.TestController;
import com.bigbro.wwooss.v1.annotation.WithMockCustomUser;
import com.bigbro.wwooss.v1.config.DocumentConfig;
import com.bigbro.wwooss.v1.dto.response.trash.TrashLogListResponse;
import com.bigbro.wwooss.v1.dto.response.trash.TrashLogResponse;
import com.bigbro.wwooss.v1.entity.trash.category.TrashCategory;
import com.bigbro.wwooss.v1.entity.trash.info.TrashInfo;
import com.bigbro.wwooss.v1.entity.trash.log.TrashLog;
import com.bigbro.wwooss.v1.entity.user.User;
import com.bigbro.wwooss.v1.enumType.TrashSize;
import com.bigbro.wwooss.v1.enumType.TrashType;
import com.bigbro.wwooss.v1.service.trash.log.TrashLogService;
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
@WebMvcTest(TrashLogApi.class)
class TrashLogApiTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TrashLogService trashLogService;

    @Test
    @WithMockCustomUser
    @DisplayName("쓰레기 로그 가져오기")
    void getTrashLog() throws Exception {
        User user = User.builder()
                .userId(1L)
                .build();
        TrashCategory trashCategory = TrashCategory.builder()
                .trashCategoryId(1L)
                .trashType(TrashType.CAN)
                .build();
        TrashInfo trashInfo = TrashInfo.builder()
                .trashInfoId(1L)
                .name("플라스틱")
                .weight(100D)
                .size(TrashSize.BIG)
                .trashImagePath("image/trash")
                .refund(100L)
                .carbonSaving(1D)
                .trashCategory(trashCategory)
                .build();
        TrashLog trashLog = TrashLog.of(user, trashInfo);
        List<TrashLogResponse> trashLogResponseList = List.of(TrashLogResponse.of(trashLog, "basePath"));
        TrashLogListResponse trashInfoResponseList =
                TrashLogListResponse.of(false, 10L, trashLogResponseList);

        given(this.trashLogService.getTrashLogList(any(), any(), any())).willReturn(trashInfoResponseList);

        this.mockMvc.perform(RestDocumentationRequestBuilders.get("/api/v1/trash-log")
                        .with(csrf().asHeader())
                        .header("Authorization", "bearer TEST_ACCESS")
                        .param("date", "2023-10")
                        .param("size", "15")
                        .param("page", "1")
                        .contextPath("/api")
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andDo(document("get-log",
                                resourceDetails().tags("쓰레기 버린 기록"),
                                DocumentConfig.getDocumentRequest(),
                                DocumentConfig.getDocumentResponse(),
                                requestParameters(
                                        parameterWithName("date").description("조회 날짜 : [null / YYYY-MM / YYYY]").optional(),
                                        parameterWithName("size").description("조회 size").optional(),
                                        parameterWithName("page").description("조회 page").optional()
                                ),
                                requestHeaders(
                                        headerWithName("Authorization").description("인증 토큰")
                                ),
                                responseFields(
                                        fieldWithPath("code").description("응답 코드"),
                                        fieldWithPath("title").description("응답 코드 별 클라이언트 노출 제목"),
                                        fieldWithPath("message").description("응답 코드 별 클라이언트 노출 메세지"),
                                        fieldWithPath("data.hasNext").description("다음 페이지 존재 여부"),
                                        fieldWithPath("data.totalCount").description("모든 로그 수"),
                                        fieldWithPath("data.trashLogResponseList[].trashLogId").description("쓰레기 기록 ID"),
                                        fieldWithPath("data.trashLogResponseList[].trashCategoryName").description("쓰레기 이름"),
                                        fieldWithPath("data.trashLogResponseList[].size").description("쓰레기 크기 - [SMALL,MEDIUM,BIG]"),
                                        fieldWithPath("data.trashLogResponseList[].trashImagePath").description("쓰레기 이미지"),
                                        fieldWithPath("data.trashLogResponseList[].createdAt").description("쓰레기 버린 날짜")
                                )
                        )
                );
    }

}
