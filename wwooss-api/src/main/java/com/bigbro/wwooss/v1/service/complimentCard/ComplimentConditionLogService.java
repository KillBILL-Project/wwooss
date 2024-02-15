package com.bigbro.wwooss.v1.service.complimentCard;

public interface ComplimentConditionLogService {

    /**
     * 로그인 로그 생성
     * @param userId
     * @return 연속 3일로그인지 아닌지
     */
    boolean createLoginLog(long userId);

    /**
     * 쓰레기 버리기 로그 생성
     */
    boolean createThrowTrashLog(long userId);

    /**
     * 리포트 상세 조회 로그
     */
    boolean createViewWeeklyLog(long userId);

    /**
     * 쓰레기 히스토리 디테일 조회 로그
     */
    boolean createViewTrashCanHistoryLog(long userId);

    /**
     * 쓰레기통 찾기 조회 로그
     */
    boolean createFindTrashCanLog(long userId);

}
