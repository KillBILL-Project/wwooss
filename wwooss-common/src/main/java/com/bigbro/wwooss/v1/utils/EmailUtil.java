package com.bigbro.wwooss.v1.utils;

import org.springframework.stereotype.Component;

@Component
public class EmailUtil {
    public String getResetPasswordEmail(String resetPassword, String logo) {
        return "<!DOCTYPE html>\n" +
                "<html lang=\"en\">\n" +
                "<head>\n" +
                "  <meta charset=\"UTF-8\">\n" +
                "  <title>WWOOSS</title>\n" +
                "</head>\n" +
                "<body style=\" margin: auto; height: 100%; width: 100%\">\n" +
                "<div style=\"padding: 20px;height: 300px;width: 100%;display:flex;flex-direction: column;align-items: flex-start;justify-content: flex-start;border: thick double white;background-color: #363636\">" +
                "<img src=\""+logo+"\" alt=\"logo\" style=\"width: 100px;height: 100px;/* margin-top: 35px; */margin-bottom: 22px;\"/>\n" +
                "<p style=\"color:white; text-align:center; with:100%; font-size: 18px\">" + "임시 비밀번호 : [ " + resetPassword + " ]" +
                "</p>" +
                "</div>" +
                "</body>\n" +
                "<footer style=\" padding-right: 59px; background-color: #DBDBDB; width: 100%;height: 50px;\">\n" +
                "</footer>\n" +
                "\n" +
                "</html>";
    }
}
