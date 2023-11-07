package com.bigbro.wwooss.v1.converter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
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

        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return Arrays.asList(objectMapper.readValue(dbData, Integer[].class));
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
