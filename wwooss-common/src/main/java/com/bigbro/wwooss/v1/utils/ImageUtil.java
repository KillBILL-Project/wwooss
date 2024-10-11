package com.bigbro.wwooss.v1.utils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class ImageUtil {

    @Value("${aws.cloud-front.path}")
    private String imageBase;

    public static final String COMPLIMENT_CARD_PATH = "compliment_card";
    public static final String TRASH_PATH = "trash";

    public static final String QUIZ_PATH = "quiz";

    public static final String WWOOSS_LOGO = "logo";

    public static final String TEAM_LOGO = "/team/logo";
    public static final String TEAM_BUSINESS_REGIT = "/team/business_register";

    public String baseComplimentCardImagePath() {
        return imageBase + COMPLIMENT_CARD_PATH + "/";
    }

    public String baseTrashImagePath() {
        return imageBase + TRASH_PATH + "/";
    }

    public String baseQuizImagePath() {
        return imageBase + QUIZ_PATH + "/";
    }

    public String wwoossBlackLogo() {return imageBase + WWOOSS_LOGO + "/" + "black_logo.png";}
}
