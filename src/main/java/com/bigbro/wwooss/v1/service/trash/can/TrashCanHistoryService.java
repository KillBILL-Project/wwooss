package com.bigbro.wwooss.v1.service.trash.can;

import com.bigbro.wwooss.v1.domain.entity.trash.can.TrashCanContents;
import com.bigbro.wwooss.v1.domain.entity.user.User;
import com.bigbro.wwooss.v1.domain.response.trash.EmptyTrashResultResponse;
import com.bigbro.wwooss.v1.domain.response.trash.TrashCanHistoryListResponse;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface TrashCanHistoryService {

    /**
     * 쓰레기통 비우기 히스토리 생성
     *
     * @param trashCanContentsList 쓰레기 내용물
     * @param user
     */
    EmptyTrashResultResponse createTrashCanHistory(List<TrashCanContents> trashCanContentsList, User user);

    /**
     * 쓰레기통 비우기 히스토리 조회

     * date 유형
     * null :  전체 데이터
     * YYYY-MM : 해당 연,월 데이터
     * YYYY : 해당 연 데이터
     * @param userId
     * @param date
     * @param pageable
     */
    TrashCanHistoryListResponse findTrashCanHistoryList(Long userId, String date, Pageable pageable);

}
