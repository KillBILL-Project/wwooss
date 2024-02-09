package com.bigbro.wwooss.v1.service.email;

import com.bigbro.wwooss.v1.dto.SendEmail;

public interface EmailService {

    /**
     * 이메일 전송 서비스
     * @param sendEmailType : EMAIL (추후 기능 생길 시 추가)
     * @param param : Email에 들어갈 값 => 추후 List 혹은 DTO 등의 형태로 변경
     */
    void sendEmail(SendEmail sendEmailType, String param);
}
