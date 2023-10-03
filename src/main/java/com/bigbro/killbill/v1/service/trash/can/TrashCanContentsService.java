package com.bigbro.killbill.v1.service.trash.can;

import com.bigbro.killbill.v1.domain.request.trash.can.TrashCanContentsRequest;

public interface TrashCanContentsService {

    /**
     * 쓰레기 버리기
     * @param trashCanContentsRequest 쓰레기 정보 ID, 쓰레기 수
     */
    void createTrashCanContents(TrashCanContentsRequest trashCanContentsRequest, Long userId);
}
