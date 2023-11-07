package com.bigbro.wwooss.v1.converter;

import com.bigbro.wwooss.v1.enumType.DayOfWeek;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter
public class DayOfWeekConverter implements AttributeConverter<List<DayOfWeek>, String> {

    @Override
    public String convertToDatabaseColumn(List<DayOfWeek> attribute) {
        if (Objects.isNull(attribute)) return null;

        return attribute.stream().map(DayOfWeek::getValue).toList().toString();
    }

    @Override
    public List<DayOfWeek> convertToEntityAttribute(String dbData) {
        if(Objects.isNull(dbData)) return null;
        ObjectMapper objectMapper = new ObjectMapper();

        try {
            List<Integer> intDayList = Arrays.asList(objectMapper.readValue(dbData, Integer[].class));
            return intDayList.stream().map(DayOfWeek::from).toList();
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
