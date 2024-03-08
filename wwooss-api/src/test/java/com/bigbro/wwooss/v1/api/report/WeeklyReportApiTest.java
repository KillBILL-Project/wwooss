package com.bigbro.wwooss.v1.api.report;

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

import com.bigbro.wwooss.v1.annotation.TestController;
import com.bigbro.wwooss.v1.annotation.WithMockCustomUser;
import com.bigbro.wwooss.v1.dto.ComplimentCardIcon;
import com.bigbro.wwooss.v1.dto.WeekInfo;
import com.bigbro.wwooss.v1.dto.WeeklyTrashByCategory;
import com.bigbro.wwooss.v1.dto.response.report.WeeklyReportDetailResponse;
import com.bigbro.wwooss.v1.dto.response.report.WeeklyReportListResponse;
import com.bigbro.wwooss.v1.dto.response.report.WeeklyReportResponse;
import com.bigbro.wwooss.v1.config.DocumentConfig;
import com.bigbro.wwooss.v1.entity.report.WeeklyReport;
import com.bigbro.wwooss.v1.entity.user.User;
import com.bigbro.wwooss.v1.enumType.Gender;
import com.bigbro.wwooss.v1.enumType.LoginType;
import com.bigbro.wwooss.v1.enumType.UserRole;
import com.bigbro.wwooss.v1.service.complimentCard.ComplimentCardService;
import com.bigbro.wwooss.v1.service.complimentCard.ComplimentConditionLogService;
import com.bigbro.wwooss.v1.service.report.WeeklyReportService;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.test.web.servlet.MockMvc;

@TestController
@WebMvcTest(WeeklyReportApi.class)
class WeeklyReportApiTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private WeeklyReportService weeklyReportService;

    @MockBean
    private ComplimentConditionLogService complimentConditionLogService;

    @MockBean
    private ComplimentCardService complimentCardService;

    @Test
    @WithMockCustomUser
    @DisplayName("쓰레기 리포트 목록 가져오기")
    void getWeeklyReportList() throws Exception {
        WeekInfo weekInfo = WeekInfo.of(2024, 4, 1);
        List<WeeklyReportResponse> weeklyReportResponseList = List.of(WeeklyReportResponse.of(1L, weekInfo,
                LocalDateTime.now(), LocalDateTime.now()));

        given(weeklyReportService.getWeeklyReport(any(), any(), any())).willReturn(WeeklyReportListResponse.of(false,
                weeklyReportResponseList));

        this.mockMvc.perform(RestDocumentationRequestBuilders.get("/api/v1/weekly-reports")
                        .with(csrf().asHeader())
                        .contextPath("/api")
                        .header("Authorization", "bearer TEST_ACCESS")
                        .param("size", "10")
                        .param("page", "0")
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andDo(document("get-weekly-reports",
                                resourceDetails().tags("주간 리포트 목록 조회"),
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
                                        fieldWithPath("data.weeklyReportResponseList[].weeklyReportId").description(
                                                "주간 리포트 ID"),
                                        fieldWithPath("data.weeklyReportResponseList[].weekInfo.year").description("조회 연도"),
                                        fieldWithPath("data.weeklyReportResponseList[].weekInfo.month").description("조회 월"),
                                        fieldWithPath("data.weeklyReportResponseList[].weekInfo.weekOfMonth").description("N주차"),
                                        fieldWithPath("data.weeklyReportResponseList[].fromDate").description("N주차 시작일"),
                                        fieldWithPath("data.weeklyReportResponseList[].toDate").description("N주차 마지막 "
                                                + "일")
                                )
                        )
                );
    }

    @Test
    @WithMockCustomUser
    @DisplayName("쓰레기 리포트 상세 조회")
    void getWeeklyReportDetail() throws Exception {
        User user = User.builder()
                .userId(1L)
                .age(1)
                .email("wwoosss@")
                .country("korea")
                .region("seoul")
                .loginType(LoginType.EMAIL)
                .gender(Gender.M)
                .refreshToken("refresh")
                .userRole(UserRole.USER)
                .build();
        WeeklyReport weeklyReport = WeeklyReport.builder()
                .weeklyReportId(1L)
                .attendanceRecord(List.of(1,2))
                .weeklyTrashCountByCategoryList(List.of(WeeklyTrashByCategory.of("플라슽틱", 1L)))
                .weeklyCarbonSaving(100D)
                .weeklyRefund(100L)
                .weeklyTrashCount(10L)
                .wowCarbonSaving(30D)
                .wowRefund(30L)
                .wowTrashCount(20L)
                .weeklyDate(LocalDateTime.now())
                .user(user)
                .build();
        List<ComplimentCardIcon> cardIcons = List.of(ComplimentCardIcon.of(1L, "image", "image title"));


        given(weeklyReportService.getWeeklyReportDetail(any(), any())).willReturn(WeeklyReportDetailResponse.of(weeklyReport, cardIcons));

        this.mockMvc.perform(RestDocumentationRequestBuilders.get("/api/v1/weekly-reports/{weekly-report-id}", 1L)
                        .with(csrf().asHeader())
                        .contextPath("/api")
                        .header("Authorization", "bearer TEST_ACCESS")
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andDo(document("get-weekly-report-detail",
                                resourceDetails().tags("주간 리포트 상세 조회"),
                                DocumentConfig.getDocumentRequest(),
                                DocumentConfig.getDocumentResponse(),
                                requestHeaders(
                                        headerWithName("Authorization").description("인증 토큰")
                                ),
                                responseFields(
                                        fieldWithPath("code").description("응답 코드"),
                                        fieldWithPath("title").description("응답 코드 별 클라이언트 노출 제목"),
                                        fieldWithPath("message").description("응답 코드 별 클라이언트 노출 메세지"),
                                        fieldWithPath("data.weeklyReportId").description("주간 리포트 ID"),
                                        fieldWithPath("data.attendanceRecord[]").description("출석 요일 [1(월) ~ 7(일)]"),
                                        fieldWithPath("data.weeklyCarbonSaving").description("주간 탄소 절감량"),
                                        fieldWithPath("data.weeklyRefund").description("주간 환급금"),
                                        fieldWithPath("data.weeklyTrashCount").description("주간 버린 쓰레기"),
                                        fieldWithPath("data.wowCarbonSaving").description("전주 대비 탄소 절감량 증감 [Week On Week]"),
                                        fieldWithPath("data.wowRefund").description("전주 대비 환불 증감 [Week On Week]"),
                                        fieldWithPath("data.wowTrashCount").description("전주 대비 버린 쓰레기 수 증감 [Week On Week]"),
                                        fieldWithPath("data.weeklyTrashCountByCategoryList[].trashCategoryName").description("쓰레기 이름"),
                                        fieldWithPath("data.weeklyTrashCountByCategoryList[].trashCount").description("쓰레기 수"),
                                        fieldWithPath("data.complimentCardIconList[].complimentCardId").description("칭찬 카드 ID"),
                                        fieldWithPath("data.complimentCardIconList[].cardImage").description("칭찬 카드 이미지"),
                                        fieldWithPath("data.complimentCardIconList[].title").description("칭찬 카드 제목")
                                )
                        )
                );
    }

}
