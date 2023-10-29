package com.bigbro.wwooss.v1.api.trash.log;

import com.bigbro.wwooss.v1.annotation.TestController;
import com.bigbro.wwooss.v1.config.DocumentConfig;
import com.bigbro.wwooss.v1.dto.response.trash.TrashLogListResponse;
import com.bigbro.wwooss.v1.dto.response.trash.TrashLogResponse;
import com.bigbro.wwooss.v1.entity.trash.info.TrashInfo;
import com.bigbro.wwooss.v1.entity.trash.log.TrashLog;
import com.bigbro.wwooss.v1.entity.user.User;
import com.bigbro.wwooss.v1.service.trash.log.TrashLogService;
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
    @DisplayName("쓰레기 정보 가져오기")
    void getTrashInfo() throws Exception {
        User user = User.builder()
                .userId(1L)
                .build();
        TrashInfo trashInfo = TrashInfo.builder()
                .trashInfoId(1L)
                .name("플라스틱")
                .refund(100L)
                .carbonEmissionPerGram(1D)
                .build();
        TrashLog trashLog = TrashLog.of(user, trashInfo, 3L, 2);
        List<TrashLogResponse> trashLogResponseList = List.of(TrashLogResponse.of(trashLog));
        TrashLogListResponse trashInfoResponseList =
                TrashLogListResponse.of(false, trashLogResponseList);

        given(this.trashLogService.getTrashLogList(any(), any(), any())).willReturn(trashInfoResponseList);

        this.mockMvc.perform(RestDocumentationRequestBuilders.get("/api/v1/trash-log")
                        .with(csrf().asHeader())
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
                                responseFields(
                                        fieldWithPath("code").description("응답 코드"),
                                        fieldWithPath("title").description("응답 코드 별 클라이언트 노출 제목"),
                                        fieldWithPath("message").description("응답 코드 별 클라이언트 노출 메세지"),
                                        fieldWithPath("data.hasNext").description("다음 페이지 존재 여부"),
                                        fieldWithPath("data.trashLogResponseList[].trashLogId").description("쓰레기 기록 ID"),
                                        fieldWithPath("data.trashLogResponseList[].trashCategoryName").description("쓰레기 이름"),
                                        fieldWithPath("data.trashLogResponseList[].size").description("버린 쓰레기 크기"),
                                        fieldWithPath("data.trashLogResponseList[].trashCount").description("버린 쓰레기 수"),
                                        fieldWithPath("data.trashLogResponseList[].createdAt").description("쓰레기 버린 날짜")
                                )
                        )
                );
    }

}
