package com.bigbro.killbill.v1.api.trash.info;

import com.bigbro.killbill.v1.annotation.TestController;
import com.bigbro.killbill.v1.config.DocumentConfig;
import com.bigbro.killbill.v1.domain.response.trash.TrashInfoResponse;
import com.bigbro.killbill.v1.service.trash.info.TrashInfoService;
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
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.requestParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@TestController
@WebMvcTest(TrashInfoApi.class)
class TrashInfoApiTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TrashInfoService trashInfoService;

    @Test
    @DisplayName("쓰레기 정보 가져오기")
    void getTrashInfo() throws Exception {
        List<TrashInfoResponse> trashInfoResponseList =
                List.of(TrashInfoResponse.builder()
                        .trashInfoId(1L)
                        .name("플라스틱")
                        .size(1)
                        .weight(1D)
                        .refund(1)
                        .carbonEmissionPerGram(1D)
                        .build());

        given(this.trashInfoService.getTrashInfoByCategoryId(1L)).willReturn(trashInfoResponseList);

        this.mockMvc.perform(RestDocumentationRequestBuilders.get("/api/v1/trash-info")
                        .param("category-id", "1")
                        .contextPath("/api")
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andDo(document("get-info-by-categoryId",
                                resourceDetails().tags("쓰레기 정보 목록 가져오기"),
                                DocumentConfig.getDocumentRequest(),
                                DocumentConfig.getDocumentResponse(),
                                requestParameters(
                                        parameterWithName("category-id").description("카테고리 ID")
                                ),
                                responseFields(
                                        fieldWithPath("code").description("응답 코드"),
                                        fieldWithPath("title").description("응답 코드 별 클라이언트 노출 제목"),
                                        fieldWithPath("message").description("응답 코드 별 클라이언트 노출 메세지"),
                                        fieldWithPath("data[].trashInfoId").description("쓰레기 정보 id"),
                                        fieldWithPath("data[].name").description("쓰레기 이름"),
                                        fieldWithPath("data[].size").description("쓰레기 크기"),
                                        fieldWithPath("data[].weight").description("쓰레기 무게"),
                                        fieldWithPath("data[].refund").description("쓰레기 환급금"),
                                        fieldWithPath("data[].carbonEmissionPerGram").description("그램당 탄소 배출 양")
                                )
                        )
                );
    }
}