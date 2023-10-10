package com.bigbro.wwooss.v1.service.trash.log;

import com.bigbro.wwooss.v1.domain.entity.trash.can.TrashCanHistory;
import com.bigbro.wwooss.v1.domain.entity.trash.info.TrashInfo;
import com.bigbro.wwooss.v1.domain.entity.user.User;

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
}
