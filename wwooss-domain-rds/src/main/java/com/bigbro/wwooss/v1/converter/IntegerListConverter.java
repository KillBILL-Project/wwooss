package com.bigbro.wwooss.v1.converter;

import org.apache.commons.lang3.StringUtils;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.util.Arrays;
import java.util.List;

@Converter
public class IntegerListConverter implements AttributeConverter<List<Integer>, String> {
    @Override
    public String convertToDatabaseColumn(List<Integer> attribute) {
        return attribute.toString();
    }

    @Override
    public List<Integer> convertToEntityAttribute(String dbData) {
        if(StringUtils.isBlank(dbData)) return null;

        String removedDbData = dbData.replaceAll("[\\[\\]]", "");

        String[] split = removedDbData.split(",");
        return Arrays.stream(split).map((value) -> Integer.parseInt(value.trim())).toList();
    }
}
