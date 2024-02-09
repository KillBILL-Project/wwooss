package com.bigbro.wwooss.v1.dto;

import com.bigbro.wwooss.v1.enumType.SendEmailType;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class SendEmail {
    String email;
    SendEmailType emailType;

    public static SendEmail of(String email, SendEmailType sendEmailType){
        return SendEmail.builder()
                .email(email)
                .emailType(sendEmailType)
                .build();
    }
}
