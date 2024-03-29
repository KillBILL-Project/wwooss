package com.bigbro.wwooss.v1.enumType;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public enum TrashType {
    PLASTIC("플라스틱"),
    PAPER("종이"),
    CAN("캔"),
    PET("페트병"),
    GLASS("병"),
    VINYL("비닐"),
    COMMON("기타");

    private String name;
}
