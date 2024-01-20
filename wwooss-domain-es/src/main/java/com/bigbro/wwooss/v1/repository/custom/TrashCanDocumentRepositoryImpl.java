package com.bigbro.wwooss.v1.repository.custom;

import com.bigbro.wwooss.v1.document.TrashCanDocument;
import com.bigbro.wwooss.v1.dto.TrashCanInfo;
import com.bigbro.wwooss.v1.exception.IncorrectDataException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.xcontent.XContentType;
import org.springframework.data.elasticsearch.core.geo.GeoPoint;

import java.io.IOException;
import java.util.List;

import static com.bigbro.wwooss.v1.response.WwoossResponseCode.DOCUMENT_BULK_INSERT_ERROR;

@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class TrashCanDocumentRepositoryImpl implements TrashCanDocumentRepositoryCustom{

    private final RestHighLevelClient highLevelClient;

    @Override
    public void saveTrashCan(List<TrashCanInfo> trashCanInfoList) {
        try {
            BulkRequest request = new BulkRequest();
            ObjectMapper objectMapper = new ObjectMapper();

            for(TrashCanInfo trashCanInfo : trashCanInfoList) {
                TrashCanDocument trashCanDocument = TrashCanDocument.of(
                        trashCanInfo.getTrashCanId(),
                        new GeoPoint(trashCanInfo.getLat(),
                                trashCanInfo.getLng()),
                        trashCanInfo.getAddress(),
                        trashCanInfo.getTrashType());

                IndexRequest indexRequest = new IndexRequest("trash-can")
                        .id(trashCanDocument.getId().toString())
                        .create(true)
                        .source(objectMapper.writeValueAsString(trashCanDocument), XContentType.JSON);
                request.add(indexRequest);
            }

            boolean existRequest = request.estimatedSizeInBytes() != 0;
            if (existRequest) {
                highLevelClient.bulk(request, RequestOptions.DEFAULT);
            };
        } catch (IOException e) {
            throw new IncorrectDataException(DOCUMENT_BULK_INSERT_ERROR);
        }
    }
}
