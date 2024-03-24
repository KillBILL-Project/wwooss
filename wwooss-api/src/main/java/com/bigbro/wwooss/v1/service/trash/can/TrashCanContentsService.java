package com.bigbro.wwooss.v1.service.trash.can;


import com.bigbro.wwooss.v1.dto.request.trash.can.TrashCanContentsRequest;
import com.bigbro.wwooss.v1.dto.response.trash.EmptyTrashResultResponse;
import com.bigbro.wwooss.v1.entity.user.User;

public interface TrashCanContentsService {

    /**
     * 쓰레기 버리기
     * @param trashCanContentsRequest 쓰레기 정보 ID, 쓰레기 수
     */
    User createTrashCanContents(TrashCanContentsRequest trashCanContentsRequest, Long userId);

    /**
     * 쓰레기통 비우기
     * 해당 User의 trash can contents 로우 모두 삭제
     *
     * @param userId
     */
    EmptyTrashResultResponse deleteTrashCanContents(Long userId);

    /**
     * 쓰레기통 내용물 갯수
     */
    Long getTrashCanContentsCount(Long userId);
}
