package com.bigbro.wwooss.v1.api.alarm;

import static com.epages.restdocs.apispec.MockMvcRestDocumentationWrapper.document;
import static com.epages.restdocs.apispec.MockMvcRestDocumentationWrapper.resourceDetails;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.bigbro.wwooss.v1.annotation.TestController;
import com.bigbro.wwooss.v1.config.DocumentConfig;
import com.bigbro.wwooss.v1.dto.request.alarm.AlarmOnOffRequest;
import com.bigbro.wwooss.v1.dto.request.alarm.AlarmRequest;
import com.bigbro.wwooss.v1.dto.request.user.UpdatePushConsentRequest;
import com.bigbro.wwooss.v1.dto.response.alarm.AlarmResponse;
import com.bigbro.wwooss.v1.enumType.DayOfWeek;
import com.bigbro.wwooss.v1.service.alarm.AlarmService;
import com.fasterxml.jackson.databind.ObjectMapper;
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
@WebMvcTest(AlarmApi.class)
public class AlarmApiTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AlarmService alarmService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    @DisplayName("알람 목록 가져오기")
    void getAlarmList() throws Exception {
        List<AlarmResponse> alarmResponses =
                List.of(AlarmResponse.builder()
                        .alarmId(1L)
                        .dayOfWeekList(List.of(DayOfWeek.MON.getValue(), DayOfWeek.FRI.getValue()))
                        .sendHour(7)
                        .sendMinute(39)
                        .on(true)
                        .build());

        given(this.alarmService.getAlarmList(1L)).willReturn(alarmResponses);

        this.mockMvc.perform(RestDocumentationRequestBuilders.get("/api/v1/alarm")
                        .with(csrf().asHeader())
                        .contextPath("/api")
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andDo(document("get-alarm-list",
                                resourceDetails().tags("알람 목록 가져오기"),
                                DocumentConfig.getDocumentRequest(),
                                DocumentConfig.getDocumentResponse(),
                                responseFields(
                                        fieldWithPath("code").description("응답 코드"),
                                        fieldWithPath("title").description("응답 코드 별 클라이언트 노출 제목"),
                                        fieldWithPath("message").description("응답 코드 별 클라이언트 노출 메세지"),
                                        fieldWithPath("data[].alarmId").description("alarm ID"),
                                        fieldWithPath("data[].dayOfWeekList").description("알람 발송 요일 - 월[1] ... 일[7]"),
                                        fieldWithPath("data[].sendHour").description("발송 시간(시)"),
                                        fieldWithPath("data[].sendMinute").description("발송 시간(분)"),
                                        fieldWithPath("data[].on").description("on/off")
                                )
                        )
                );
    }

    @Test
    @DisplayName("알람 생성")
    void createAlarm() throws Exception {
        AlarmRequest alarmRequest = AlarmRequest.builder()
                .dayOfWeekList(List.of(DayOfWeek.MON.getValue(), DayOfWeek.FRI.getValue()))
                .sendHour(7)
                .sendMinute(39)
                .build();

        AlarmResponse alarmResponse = AlarmResponse.builder()
                .alarmId(1L)
                .dayOfWeekList(List.of(DayOfWeek.MON.getValue(), DayOfWeek.FRI.getValue()))
                .sendHour(7)
                .sendMinute(39)
                .on(true)
                .build();

        given(this.alarmService.createAlarm(any(), any())).willReturn(alarmResponse);

        this.mockMvc.perform(RestDocumentationRequestBuilders.post("/api/v1/alarm")
                        .with(csrf().asHeader())
                        .contextPath("/api")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(alarmRequest))
                )
                .andExpect(status().isCreated())
                .andDo(document("create-alarm",
                                resourceDetails().tags("알람 생성"),
                                DocumentConfig.getDocumentRequest(),
                                DocumentConfig.getDocumentResponse(),
                                responseFields(
                                        fieldWithPath("code").description("응답 코드"),
                                        fieldWithPath("title").description("응답 코드 별 클라이언트 노출 제목"),
                                        fieldWithPath("message").description("응답 코드 별 클라이언트 노출 메세지"),
                                        fieldWithPath("data.alarmId").description("alarm ID"),
                                        fieldWithPath("data.dayOfWeekList").description("알람 발송 요일 - 월[1] ... 일[7]"),
                                        fieldWithPath("data.sendHour").description("발송 시간(시)"),
                                        fieldWithPath("data.sendMinute").description("발송 시간(분)"),
                                        fieldWithPath("data.on").description("on/off")
                                )
                        )
                );
    }

    @Test
    @DisplayName("알람 on/off")
    void switchAlarm() throws Exception {
        AlarmOnOffRequest alarmOnOffRequest = AlarmOnOffRequest.builder()
                .isOn(true)
                .build();

        this.mockMvc.perform(RestDocumentationRequestBuilders.patch("/api/v1/alarm/{alarm-id}/on-off", 1L)
                        .with(csrf().asHeader())
                        .contextPath("/api")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(alarmOnOffRequest))
                )
                .andExpect(status().isOk())
                .andDo(document("switch-alarm",
                                resourceDetails().tags("알람 on/off"),
                                DocumentConfig.getDocumentRequest(),
                                DocumentConfig.getDocumentResponse(),
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
    @DisplayName("알람 update")
    void updateAlarm() throws Exception {
        AlarmRequest alarmRequest = AlarmRequest.builder()
                .dayOfWeekList(List.of(DayOfWeek.MON.getValue(), DayOfWeek.FRI.getValue()))
                .sendHour(7)
                .sendMinute(39)
                .build();

        AlarmResponse alarmResponse = AlarmResponse.builder()
                .alarmId(1L)
                .dayOfWeekList(List.of(DayOfWeek.MON.getValue(), DayOfWeek.FRI.getValue()))
                .sendHour(7)
                .sendMinute(39)
                .on(true)
                .build();

        given(alarmService.updateAlarm(any(), any())).willReturn(alarmResponse);

        this.mockMvc.perform(RestDocumentationRequestBuilders.patch("/api/v1/alarm/{alarm-id}", 1L)
                        .with(csrf().asHeader())
                        .contextPath("/api")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(alarmRequest))
                )
                .andExpect(status().isOk())
                .andDo(document("update-alarm",
                                resourceDetails().tags("알람 업데이트"),
                                DocumentConfig.getDocumentRequest(),
                                DocumentConfig.getDocumentResponse(),
                                responseFields(
                                        fieldWithPath("code").description("응답 코드"),
                                        fieldWithPath("title").description("응답 코드 별 클라이언트 노출 제목"),
                                        fieldWithPath("message").description("응답 코드 별 클라이언트 노출 메세지"),
                                        fieldWithPath("data.alarmId").description("alarm ID"),
                                        fieldWithPath("data.dayOfWeekList").description("알람 발송 요일 - 월[1] ... 일[7]"),
                                        fieldWithPath("data.sendHour").description("발송 시간(시)"),
                                        fieldWithPath("data.sendMinute").description("발송 시간(분)"),
                                        fieldWithPath("data.on").description("on/off")
                                )
                        )
                );
    }
}
