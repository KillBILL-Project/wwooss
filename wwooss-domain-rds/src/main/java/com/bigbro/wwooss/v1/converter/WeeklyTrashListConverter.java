package com.bigbro.wwooss.v1.converter;

import com.bigbro.wwooss.v1.dto.WeeklyTrashByCategory;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.StringUtils;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.util.Arrays;
import java.util.List;



@Converter
public class WeeklyTrashListConverter implements AttributeConverter<List<WeeklyTrashByCategory>, String> {
    @Override
    public String convertToDatabaseColumn(List<WeeklyTrashByCategory> attribute) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.writeValueAsString(attribute);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<WeeklyTrashByCategory> convertToEntityAttribute(String dbData) {
        if(StringUtils.isBlank(dbData)) return null;

        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return Arrays.asList(objectMapper.readValue(dbData, WeeklyTrashByCategory[].class));
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
