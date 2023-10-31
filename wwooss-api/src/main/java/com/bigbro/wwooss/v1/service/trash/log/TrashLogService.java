package com.bigbro.wwooss.v1.service.trash.log;

import com.bigbro.wwooss.v1.dto.response.trash.TrashLogListResponse;
import com.bigbro.wwooss.v1.entity.trash.can.TrashCanHistory;
import com.bigbro.wwooss.v1.entity.trash.info.TrashInfo;
import com.bigbro.wwooss.v1.entity.user.User;
import org.springframework.data.domain.Pageable;

public interface TrashLogService {

    /**
     * 로그 생성 API
     * @param user
     * @param trashInfo
     * @param trashCount
     * @param size
     */
    void createTrashLog(User user, TrashInfo trashInfo, Long trashCount, Integer size);

    /**
     * 쓰레기통 비우기 히스토리 ID 추가
     * trashCanHistory 쓰레기통 비우기 히스토리
     * user
     */
    void updateTrashLogTrashHistory(TrashCanHistory trashCanHistory, User user);

    /**
     * 월단위 쓰레기 로그 가져오기
     * date 유형
     * null :  전체 데이터
     * YYYY-MM : 해당 연,월 데이터
     * YYYY : 해당 연 데이터
     * @param pageable
     * @param userId
     * @param date
     */
    TrashLogListResponse getTrashLogList(Long userId, String date, Pageable pageable);
}
