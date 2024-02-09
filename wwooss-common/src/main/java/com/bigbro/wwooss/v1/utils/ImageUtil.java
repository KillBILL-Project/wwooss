package com.bigbro.wwooss.v1.utils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class ImageUtil {

    @Value("${aws.cloud-front.path}")
    private String imageBase;

    private final String COMPLIMENT_CARD_PATH = "compliment_card";
    private final String TRASH_PATH = "trash";

    private final String WWOOSS_LOGO = "logo";

    public String baseComplimentCardImagePath() {
        return imageBase + COMPLIMENT_CARD_PATH + "/";
    }

    public String baseTrashImagePath() {
        return imageBase + TRASH_PATH + "/";
    }

    public String wwoossBlackLogo() {return imageBase + WWOOSS_LOGO + "/" + "black_logo.png";}
}
