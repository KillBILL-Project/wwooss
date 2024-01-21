package com.bigbro.wwooss.v1.api.trash.category;

import com.bigbro.wwooss.v1.annotation.TestController;
import com.bigbro.wwooss.v1.config.DocumentConfig;
import com.bigbro.wwooss.v1.dto.request.trash.category.TrashCategoryRequest;
import com.bigbro.wwooss.v1.dto.response.trash.TrashCategoryResponse;
import com.bigbro.wwooss.v1.enumType.TrashType;
import com.bigbro.wwooss.v1.service.trash.impl.TrashCategoryServiceImpl;
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
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@TestController
@WebMvcTest(TrashCategoryApi.class)
class TrashCategoryApiTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TrashCategoryServiceImpl trashCategoryService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    @DisplayName("쓰레기 카테고리 가져오기")
    void getTrashCategory() throws Exception {
        List<TrashCategoryResponse> trashCategoryResponses =
                List.of(TrashCategoryResponse.builder()
                        .trashCategoryId(1L)
                        .trashCategoryName(TrashType.PLASTIC.getName())
                        .build());

        given(this.trashCategoryService.getTrashCategories()).willReturn(trashCategoryResponses);

        this.mockMvc.perform(RestDocumentationRequestBuilders.get("/api/v1/trash-categories")
                        .with(csrf().asHeader())
                        .contextPath("/api")
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andDo(document("get-trash-categories",
                            resourceDetails().tags("쓰레기 카테고리 가져오기"),
                            DocumentConfig.getDocumentRequest(),
                            DocumentConfig.getDocumentResponse(),
                            responseFields(
                                    fieldWithPath("code").description("응답 코드"),
                                    fieldWithPath("title").description("응답 코드 별 클라이언트 노출 제목"),
                                    fieldWithPath("message").description("응답 코드 별 클라이언트 노출 메세지"),
                                    fieldWithPath("data[].trashCategoryId").description("쓰레기 카테고리 id"),
                                    fieldWithPath("data[].trashCategoryName").description("쓰레기 카테고리 이름")
                            )
                        )
                );
    }

    @Test
    @DisplayName("쓰레기 카테고리 생성")
    void createTrashCategory() throws Exception {
        TrashCategoryRequest trashCategoryRequest = TrashCategoryRequest.builder()
                .trashType(TrashType.PAPER)
                .build();


        this.mockMvc.perform(RestDocumentationRequestBuilders.post("/api/v1/trash-categories")
                        .with(csrf().asHeader())
                        .contextPath("/api")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(trashCategoryRequest))
                )
                .andExpect(status().isCreated())
                .andDo(document("create-trash-categoies",
                                resourceDetails().tags("쓰레기 카테고리 생성"),
                                DocumentConfig.getDocumentRequest(),
                                DocumentConfig.getDocumentResponse(),
                                requestFields(
                                        fieldWithPath("trashType").description(
                                                "쓰레기 타입 - PAPER[종이] / CAN[캔] / PLASTIC[플라스틱] / PET[페트병] / VESSEL[용기]"),
                                        fieldWithPath("counselAt").description("상담 일시")
                                ),
                                responseFields(
                                        fieldWithPath("code").description("응답 코드"),
                                        fieldWithPath("title").description("응답 코드 별 클라이언트 노출 제목"),
                                        fieldWithPath("message").description("응답 코드 별 클라이언트 노출 메세지"),
                                        fieldWithPath("data").description("데이터 없음")
                                )
                        )
                );
    }
}
