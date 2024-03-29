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

    @Field(type = FieldType.Text)
    private String placeName;

    // ,로 구분 PAPER,CAN,PAPER
    @Field(type = FieldType.Text)
    private String trashType;

    public static TrashCanDocument of(Long id, GeoPoint location, String address, String placeName, String trashType) {
        return TrashCanDocument.builder()
                .id(id)
                .location(location)
                .address(address)
                .placeName(placeName)
                .trashType(trashType)
                .build();
    }

}
