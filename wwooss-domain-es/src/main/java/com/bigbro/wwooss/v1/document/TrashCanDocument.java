package com.bigbro.wwooss.v1.document;

import com.bigbro.wwooss.v1.enumType.TrashType;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;
import org.springframework.data.elasticsearch.annotations.GeoPointField;
import org.springframework.data.elasticsearch.annotations.Setting;
import org.springframework.data.elasticsearch.core.geo.GeoPoint;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Document(indexName = "trash-can")
@Setting(settingPath = "elasticsearch/analyzer-config.json")
public class TrashCanDocument {

    @Id
    private Long id;

    @Field(type = FieldType.Object)
    @GeoPointField
    private GeoPoint location;

    @Field(type = FieldType.Text)
    private String address;

    @Field(type = FieldType.Keyword)
    private TrashType trashType;

    public static TrashCanDocument of(Long id, GeoPoint location, String address, TrashType trashType) {
        return TrashCanDocument.builder()
                .id(id)
                .location(location)
                .address(address)
                .trashType(trashType)
                .build();
    }

}
