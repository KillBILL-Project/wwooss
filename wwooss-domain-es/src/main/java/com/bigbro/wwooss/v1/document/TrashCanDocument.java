package com.bigbro.wwooss.v1.document;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;
import org.springframework.data.elasticsearch.annotations.Setting;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Document(indexName = "trash-can")
@Setting(settingPath = "elastic/analyzer-config.json")
public class TrashCanDocument {
    @Id
    private Long id;

    @Field(type = FieldType.Keyword)
    private String standardDate;

    @Field(type = FieldType.Long)
    private Long lng;

    @Field(type = FieldType.Long)
    private Long lat;

    @Field(type = FieldType.Text)
    private String address;

    @Field(type = FieldType.Keyword)
    private String trashCategory;

}
