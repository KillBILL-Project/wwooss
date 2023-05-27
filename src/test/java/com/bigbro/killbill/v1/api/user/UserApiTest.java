package com.bigbro.killbill.v1.api.user;

import com.bigbro.killbill.v1.annotation.TestController;
import com.bigbro.killbill.v1.config.DocumentConfig;
import com.bigbro.killbill.v1.domain.response.user.GetUserResponse;
import com.bigbro.killbill.v1.service.user.UserService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;

import static com.epages.restdocs.apispec.MockMvcRestDocumentationWrapper.document;
import static com.epages.restdocs.apispec.MockMvcRestDocumentationWrapper.resourceDetails;
import static org.mockito.BDDMockito.given;

import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.test.web.servlet.MockMvc;


import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@TestController
@WebMvcTest(UserApi.class)
class UserApiTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @Test
    @DisplayName("test")
    void test() throws Exception {
        given(this.userService.test()).willReturn(
                GetUserResponse.builder()
                        .userId(1L)
                        .userName("YSW")
                        .build()
        );

        this.mockMvc.perform(RestDocumentationRequestBuilders.get("/api/v1/user")
                        .contextPath("/api")
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andDo(document("kill test",
                            resourceDetails().tags("test"),
                            DocumentConfig.getDocumentRequest(),
                            DocumentConfig.getDocumentResponse(),
                            responseFields(
                                    fieldWithPath("userId").description("user id"),
                                    fieldWithPath("userName").description("user name")
                            )
                        )
                );
    }
}