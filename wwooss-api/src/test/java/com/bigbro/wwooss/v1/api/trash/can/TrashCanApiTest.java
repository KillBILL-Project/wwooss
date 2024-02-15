package com.bigbro.wwooss.v1.api.trash.can;

import com.bigbro.wwooss.v1.annotation.TestController;
import com.bigbro.wwooss.v1.annotation.WithMockCustomUser;
import com.bigbro.wwooss.v1.config.DocumentConfig;
import com.bigbro.wwooss.v1.dto.response.trash.TrashCanResponse;
import com.bigbro.wwooss.v1.service.complimentCard.ComplimentCardService;
import com.bigbro.wwooss.v1.service.complimentCard.ComplimentConditionLogService;
import com.bigbro.wwooss.v1.service.es.TrashCanDocumentService;
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
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.requestParameters;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@TestController
@WebMvcTest(TrashCanApi.class)
class TrashCanApiTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TrashCanDocumentService trashCanDocumentService;

    @MockBean
    private ComplimentCardService complimentCardService;

    @MockBean
    private ComplimentConditionLogService complimentConditionLogService;

    @Test
    @WithMockCustomUser
    @DisplayName("쓰레기통 위치 검색")
    void createTrashCanContents() throws Exception {
        List<TrashCanResponse> responses = List.of(TrashCanResponse.of(
                1L, 37.53519607656067D, 126.82551415214544D, "강서구 개화산역", List.of("PLASTIC", "CAN")));

        given(trashCanDocumentService.findTrashCanByGeoLocation(any(), any(), any(), any())).willReturn(responses);

        this.mockMvc.perform(RestDocumentationRequestBuilders.get("/api/v1/trash-can")
                        .with(csrf().asHeader())
                        .contextPath("/api")
                        .header("Authorization", "bearer TEST_ACCESS")
                        .param("lat", "37.53519607656067D")
                        .param("lng", "126.82551415214544D")
                        .param("distance", "100")
                        .param("trashType", "PLASTIC,CAN")
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andDo(document("trash-can-by-geo-location",
                                resourceDetails().tags("쓰레기통 위치 검색"),
                                DocumentConfig.getDocumentRequest(),
                                DocumentConfig.getDocumentResponse(),
                                requestParameters(
                                        parameterWithName("lat").description("화면 중심 위도"),
                                        parameterWithName("lng").description("화면 중심 경도"),
                                        parameterWithName("distance").description("화면중심을 기준으로 검색하고자 하는 반경 - 단위 [KM]"),
                                        parameterWithName("trashType").description("쓰레기통 타입 - [PAPER[종이] / CAN[캔] / PLASTIC[플라스틱] / PET[페트병] / GLASS[병] / VINYL[비닐] / COMMON[기타]/ VASSEL[빈용기] /Null[전체검색]]").optional()
                                ),
                                requestHeaders(
                                        headerWithName("Authorization").description("인증 토큰")
                                ),
                                responseFields(
                                        fieldWithPath("code").description("응답 코드"),
                                        fieldWithPath("title").description("응답 코드 별 클라이언트 노출 제목"),
                                        fieldWithPath("message").description("응답 코드 별 클라이언트 노출 메세지"),
                                        fieldWithPath("data[].trashCanId").description("쓰레기통 ID"),
                                        fieldWithPath("data[].lat").description("위도"),
                                        fieldWithPath("data[].lng").description("경도"),
                                        fieldWithPath("data[].address").description("주소"),
                                        fieldWithPath("data[].trashType[]").description("쓰레기통 타입")
                                )
                        )
                );
    }

}
