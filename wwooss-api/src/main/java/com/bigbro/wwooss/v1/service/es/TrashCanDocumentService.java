package com.bigbro.wwooss.v1.service.es;

import com.bigbro.wwooss.v1.dto.response.trash.TrashCanResponse;

import java.util.List;

public interface TrashCanDocumentService {

    /**
     * rdb의 쓰레기통 데이터를 기반으로 한
     * document 생성
     */
    void migrationTrashCanDocument();

    /**
     * 내 주변 쓰레기통 정보 가져오기
     * trashType : 쓰레기 타입 -> ,로 구분 PLASTIC,CAN
     * distance : 내 기준 반경거리 단위 - KM
     */
    List<TrashCanResponse> findTrashCanByGeoLocation(Double lat, Double lng, Double distance, String trashType);
}
