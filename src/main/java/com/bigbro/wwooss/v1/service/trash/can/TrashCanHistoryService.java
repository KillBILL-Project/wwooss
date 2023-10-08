package com.bigbro.wwooss.v1.service.trash.can;

import com.bigbro.wwooss.v1.domain.entity.trash.can.TrashCanContents;
import com.bigbro.wwooss.v1.domain.entity.user.User;

import java.util.List;

public interface TrashCanHistoryService {

    /**
     * 쓰레기통 비우기 히스토리 생성
     *
     * @param trashCanContentsList 쓰레기 내용물
     * @param user
     */
    void createTrashCanHistory(List<TrashCanContents> trashCanContentsList, User user);
}
