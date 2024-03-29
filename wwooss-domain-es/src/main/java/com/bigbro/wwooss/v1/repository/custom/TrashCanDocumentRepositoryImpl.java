package com.bigbro.wwooss.v1.repository.custom;

import com.bigbro.wwooss.v1.document.TrashCanDocument;
import com.bigbro.wwooss.v1.dto.TrashCanInfo;
import com.bigbro.wwooss.v1.exception.IncorrectDataException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.unit.DistanceUnit;
import org.elasticsearch.index.query.MatchQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.xcontent.XContentType;
import org.springframework.data.elasticsearch.core.geo.GeoPoint;
import org.elasticsearch.index.query.GeoDistanceQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchOperations;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.lang.Nullable;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

import static com.bigbro.wwooss.v1.response.WwoossResponseCode.DOCUMENT_BULK_INSERT_ERROR;

@Slf4j
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class TrashCanDocumentRepositoryImpl implements TrashCanDocumentRepositoryCustom {

    private static final Double DEFAULT_DISTANCE = 50D;

    private final RestHighLevelClient highLevelClient;

    private final SearchOperations searchOperations;

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
                        trashCanInfo.getPlaceName(),
                        trashCanInfo.getTrashType());

                IndexRequest indexRequest = new IndexRequest("trash-can")
                        .id(trashCanDocument.getId().toString())
                        .create(true)
                        .source(objectMapper.writeValueAsString(trashCanDocument), XContentType.JSON);
                request.add(indexRequest);
            }

            boolean existRequest = request.estimatedSizeInBytes() != 0;
            if (existRequest) {
                BulkResponse bulk = highLevelClient.bulk(request, RequestOptions.DEFAULT);
                if (bulk.hasFailures()) {
                    log.error("bulk insert error response : {}", bulk.buildFailureMessage());
                    throw new IOException();
                }
            };
        } catch (IOException e) {
            throw new IncorrectDataException(DOCUMENT_BULK_INSERT_ERROR);
        }
    }

    @Override
    public List<TrashCanDocument> findByGeoLocationAndTrashType(Double lat, Double lng, Double distance, @Nullable String trashType) {
        if(Objects.isNull(distance)) {
            distance = DEFAULT_DISTANCE;
        }
        GeoDistanceQueryBuilder geoDistanceQueryBuilder = QueryBuilders.geoDistanceQuery("location")
                .point(lat, lng)
                .distance(distance, DistanceUnit.KILOMETERS);

        QueryBuilder search;
        if(StringUtils.isBlank(trashType)) {
            search = QueryBuilders.boolQuery().must(geoDistanceQueryBuilder);
        } else {
            MatchQueryBuilder matchQueryBuilder = QueryBuilders.matchQuery("trashType", trashType);
            search = QueryBuilders.boolQuery().must(matchQueryBuilder).must(geoDistanceQueryBuilder);
        }

        NativeSearchQuery searchQuery = new NativeSearchQuery(search);
        List<SearchHit<TrashCanDocument>> searchHits = searchOperations.search(searchQuery, TrashCanDocument.class).getSearchHits();
        return searchHits.stream().map(SearchHit::getContent).toList();
    }
}
