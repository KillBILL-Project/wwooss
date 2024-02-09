package com.bigbro.wwooss.v1.utils;

import org.springframework.stereotype.Component;

import java.security.SecureRandom;

@Component
public class WwoossUtil {
    private static final String ELEMENT_PASSWORD = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789@!#$%&_-";

    public String makeRandomPw(int size) {

        SecureRandom random = new SecureRandom();
        StringBuffer sb = new StringBuffer();

        for(int i = 0; i < size; i++) {
            //무작위로 문자열의 인덱스 반환
            int index = random.nextInt(ELEMENT_PASSWORD.length());
            //index의 위치한 랜덤값
            sb.append(ELEMENT_PASSWORD.charAt(index));
        }

        return sb.toString();
    }
}
