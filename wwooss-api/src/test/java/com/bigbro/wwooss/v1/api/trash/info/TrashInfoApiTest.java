package com.bigbro.wwooss.v1.api.trash.info;

import com.bigbro.wwooss.v1.annotation.TestController;
import com.bigbro.wwooss.v1.config.DocumentConfig;
import com.bigbro.wwooss.v1.dto.request.trash.info.TrashInfoRequest;
import com.bigbro.wwooss.v1.dto.response.trash.TrashInfoResponse;
import com.bigbro.wwooss.v1.enumType.TrashSize;
import com.bigbro.wwooss.v1.enumType.TrashType;
import com.bigbro.wwooss.v1.service.trash.info.TrashInfoService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static com.epages.restdocs.apispec.MockMvcRestDocumentationWrapper.document;
import static com.epages.restdocs.apispec.MockMvcRestDocumentationWrapper.resourceDetails;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@TestController
@WebMvcTest(TrashInfoApi.class)
class TrashInfoApiTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TrashInfoService trashInfoService;

    private final ObjectMapper objectMapper = new ObjectMapper();


    @Test
    @DisplayName("쓰레기 메타정보 가져오기")
    void getTrashInfoMeta() throws Exception {
        List<TrashInfoResponse> trashInfoResponses = List.of(
                TrashInfoResponse.builder()
                        .trashInfoId(1L)
                        .trashCategoryName(TrashType.CAN)
                        .trashImagePath("image/can")
                        .refund(10L)
                        .size(TrashSize.BIG)
                        .build()
        );

        given(this.trashInfoService.getTrashInfo()).willReturn(trashInfoResponses);

        this.mockMvc.perform(RestDocumentationRequestBuilders.get("/api/v1/trash-info")
                .with(csrf().asHeader())
                .contextPath("/api")
                .contentType(MediaType.APPLICATION_JSON)
        )
        .andExpect(status().isOk())
        .andDo(document("get-trash-info",
                resourceDetails().tags("쓰레기 메타 정보 가져오기"),
                DocumentConfig.getDocumentRequest(),
                DocumentConfig.getDocumentResponse(),
                requestHeaders(
                        headerWithName("Authorization").description("인증 토큰")
                ),
                responseFields(
                        fieldWithPath("code").description("응답 코드"),
                        fieldWithPath("title").description("응답 코드 별 클라이언트 노출 제목"),
                        fieldWithPath("message").description("응답 코드 별 클라이언트 노출 메세지"),
                        fieldWithPath("data[].trashInfoId").description("쓰레기 메타 정보 ID"),
                        fieldWithPath("data[].trashCategoryName").description("쓰레기 타입"),
                        fieldWithPath("data[].trashImagePath").description("쓰레기 이미지"),
                        fieldWithPath("data[].refund").description("쓰레기 환불 금액"),
                        fieldWithPath("data[].size").description("쓰레기 크기 - [SMALL/MEDIUM/BIG]")
                )));
    }

    @Test
    @DisplayName("쓰레기 정보 생성")
    void createTrashCategory() throws Exception {
        TrashInfoRequest trashInfoRequest = TrashInfoRequest.builder()
                .name("플라스틱")
                .weight(1D)
                .refund(100L)
                .size(TrashSize.BIG)
                .carbonSaving(12D)
                .trashCategoryId(1L)
                .trashImagePath("imagepath")
                .build();

        this.mockMvc.perform(RestDocumentationRequestBuilders.post("/api/v1/trash-info")
                        .with(csrf().asHeader())
                        .contextPath("/api")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(trashInfoRequest))
                )
                .andExpect(status().isCreated())
                .andDo(document("create-trash-info",
                                resourceDetails().tags("쓰레기 정보 생성"),
                                DocumentConfig.getDocumentRequest(),
                                DocumentConfig.getDocumentResponse(),
                                requestFields(
                                        fieldWithPath("name").type(JsonFieldType.STRING).description("쓰레기 이름"),
                                        fieldWithPath("weight").type(JsonFieldType.NUMBER).description("쓰레기 무게"),
                                        fieldWithPath("refund").type(JsonFieldType.NUMBER).description("쓰레기 환급금"),
                                        fieldWithPath("size").type(JsonFieldType.STRING).description("쓰레기 크기 - [BIG]/[MEDIUM]/[SMALL]"),
                                        fieldWithPath("carbonSaving").type(JsonFieldType.NUMBER).description("탄소 절감량"),
                                        fieldWithPath("trashCategoryId").type(JsonFieldType.NUMBER).description("쓰레기 카테고리 ID"),
                                        fieldWithPath("trashImagePath").type(JsonFieldType.STRING).description("쓰레기 이미지")
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
}
