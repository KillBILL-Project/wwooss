package com.bigbro.wwooss.v1.service.es;

public interface TrashCanDocumentService {

    /**
     * rdb의 쓰레기통 데이터를 기반으로 한
     * document 생성
     */
    void migrationTrashCanDocument();
}
