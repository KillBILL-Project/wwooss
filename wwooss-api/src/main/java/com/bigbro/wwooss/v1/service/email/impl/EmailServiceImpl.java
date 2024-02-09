package com.bigbro.wwooss.v1.service.email.impl;

import com.bigbro.wwooss.v1.dto.SendEmail;
import com.bigbro.wwooss.v1.exception.IncorrectDataException;
import com.bigbro.wwooss.v1.exception.SendEmailException;
import com.bigbro.wwooss.v1.service.email.EmailService;
import com.bigbro.wwooss.v1.utils.EmailUtil;
import com.bigbro.wwooss.v1.utils.ImageUtil;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.internet.MimeMessage;

import static com.bigbro.wwooss.v1.response.WwoossResponseCode.INCORRECT_DATA;
import static com.bigbro.wwooss.v1.response.WwoossResponseCode.INTERNAL_SERVER_ERROR;

@Service
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class EmailServiceImpl implements EmailService {

    private final EmailUtil emailUtil;

    private final JavaMailSender mailSender;

    private final ImageUtil imageUtil;

    @Override
    public void sendEmail(SendEmail sendEmail, String param) {
        String mailTemplate;
        switch (sendEmail.getEmailType()) {
            case RESET_PASSWORD -> {
                mailTemplate = emailUtil.getResetPasswordEmail(param, imageUtil.wwoossBlackLogo());
            }
            default -> throw new IncorrectDataException(INCORRECT_DATA, "지원하지 않는 이메일 타입입니다.");
        }
        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper =
                    new MimeMessageHelper(mimeMessage, "utf-8");
            helper.setText(mailTemplate, true);
            helper.setTo(sendEmail.getEmail());
            helper.setSubject("[WWOOSS] 임시 비밀번호 발급");
            helper.setFrom("wwooss");
            mailSender.send(mimeMessage);
        } catch (Exception e) {
            throw new SendEmailException(INTERNAL_SERVER_ERROR, "이메일 전송 실패");
        }
    }
}
