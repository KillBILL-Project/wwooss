package com.bigbro.wwooss.v1.service.trash.impl;

import com.bigbro.wwooss.v1.dto.CarbonSavingByTrashCategory;
import com.bigbro.wwooss.v1.dto.RefundByTrashCategory;
import com.bigbro.wwooss.v1.dto.TrashOutput;
import com.bigbro.wwooss.v1.dto.response.trash.EmptyTrashResultResponse;
import com.bigbro.wwooss.v1.dto.response.trash.TrashCanHistoryListResponse;
import com.bigbro.wwooss.v1.dto.response.trash.TrashCanHistoryResponse;
import com.bigbro.wwooss.v1.entity.trash.can.TrashCanContents;
import com.bigbro.wwooss.v1.entity.trash.can.TrashCanHistory;
import com.bigbro.wwooss.v1.entity.trash.info.TrashInfo;
import com.bigbro.wwooss.v1.entity.trash.log.TrashLog;
import com.bigbro.wwooss.v1.entity.user.User;
import com.bigbro.wwooss.v1.enumType.TrashType;
import com.bigbro.wwooss.v1.exception.DataNotFoundException;
import com.bigbro.wwooss.v1.repository.trash.can.TrashCanHistoryRepository;
import com.bigbro.wwooss.v1.repository.trash.log.TrashLogRepository;
import com.bigbro.wwooss.v1.repository.user.UserRepository;
import com.bigbro.wwooss.v1.response.WwoossResponseCode;
import com.bigbro.wwooss.v1.service.trash.can.TrashCanHistoryService;
import com.bigbro.wwooss.v1.service.trash.log.TrashLogService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;

@Service
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class TrashCanHistoryServiceImpl implements TrashCanHistoryService {

    private final TrashCanHistoryRepository trashCanHistoryRepository;

    private final TrashLogService trashLogService;

    private final UserRepository userRepository;

    private final TrashLogRepository trashLogRepository;

    @Override
    @Transactional
    public EmptyTrashResultResponse createTrashCanHistory(List<TrashCanContents> trashCanContentsList, User user) {
        List<TrashInfo> trashInfoList = trashCanContentsList.stream().map(TrashCanContents::getTrashInfo).toList();
        TrashOutput trashOutput = getTrashOutput(trashInfoList);

        TrashCanHistory savedTrashCanHistory = trashCanHistoryRepository.save(TrashCanHistory.of(trashOutput.getTotalCarbonSaving(), trashOutput.getTotalRefund(), user));
        trashLogService.updateTrashLogTrashHistory(savedTrashCanHistory, user);

        return getEmptyTrashResultResponse(trashOutput, savedTrashCanHistory.getTrashCanHistoryId());
    }

    private TrashOutput getTrashOutput(List<TrashInfo> trashLogList) {
        long totalRefund = 0;
        double totalCarbonSaving = 0;

        // 카테고리별 환급금 총합
        HashMap<TrashType, Long> refundByCategory = new HashMap<>();
        // 카테고리별 탄소 절감량 총합
        HashMap<TrashType, Double> carbonSavingByCategory = new HashMap<>();

        for (TrashInfo trashInfo : trashLogList) {
            // 카테고리별 탄소배출량, 환급금 합 계산
            TrashType trashType = trashInfo.getTrashCategory().getTrashType();
            refundByCategory.put(trashType, refundByCategory.getOrDefault(trashType, 0L) + trashInfo.getRefund());
            carbonSavingByCategory.put(trashType, carbonSavingByCategory.getOrDefault(trashType, 0D) + trashInfo.getCarbonSaving());

            totalCarbonSaving += trashInfo.getCarbonSaving();
            totalRefund += trashInfo.getRefund();
        }
        return TrashOutput.of(totalRefund, totalCarbonSaving, refundByCategory, carbonSavingByCategory);
    }


    private EmptyTrashResultResponse getEmptyTrashResultResponse(TrashOutput trashOutput,
                                                                 Long trashHistoryId) {
        List<RefundByTrashCategory> refundByTrashCategoryList = trashOutput.getRefundByCategory().keySet().stream()
                .map((categoryName) ->
                        RefundByTrashCategory.of(categoryName, trashOutput.getRefundByCategory().get(categoryName))
                ).toList();

        List<CarbonSavingByTrashCategory> carbonSavingByTrashCategoryList = trashOutput.getCarbonSavingByCategory().keySet().stream()
                .map((categoryName) ->
                        CarbonSavingByTrashCategory.of(categoryName, trashOutput.getCarbonSavingByCategory().get(categoryName))
                ).toList();

        return EmptyTrashResultResponse.of(trashOutput.getTotalCarbonSaving(), carbonSavingByTrashCategoryList, trashOutput.getTotalRefund(), refundByTrashCategoryList, trashHistoryId);
    }

    @Override
    @Transactional(readOnly = true)
    public TrashCanHistoryListResponse findTrashCanHistoryList(Long userId, String date, Pageable pageable) {
        User user = userRepository.findById(userId).orElseThrow(() -> new DataNotFoundException(WwoossResponseCode.NOT_FOUND_DATA, "존재하지 않는 유저입니다."));
        Slice<TrashCanHistory> trashCanHistories = trashCanHistoryRepository.findTrashCanHistoriesByUserAndDate(user, date, pageable);
        List<TrashCanHistoryResponse> trashCanHistoryResponseList = trashCanHistories.stream().map((TrashCanHistoryResponse::from)).toList();

        return TrashCanHistoryListResponse.of(trashCanHistories.hasNext(), trashCanHistoryResponseList);
    }

    @Override
    @Transactional(readOnly = true)
    public EmptyTrashResultResponse findTrashCanHistoryDetail(Long userId, Long trashCanHistoryId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new DataNotFoundException(WwoossResponseCode.NOT_FOUND_DATA, "존재하지 않는 유저입니다."));
        TrashCanHistory trashCanHistory = trashCanHistoryRepository.findTrashCanHistoryByUserAndTrashCanHistoryId(user, trashCanHistoryId)
                .orElseThrow(() -> new DataNotFoundException(WwoossResponseCode.NOT_FOUND_DATA, "존재하지 않는 비우기 항목입니다."));

        List<TrashLog> trashLogs = trashLogRepository.findTrashLogByUserAndTrashCanHistory(user, trashCanHistory);
        List<TrashInfo> trashInfoList = trashLogs.stream().map(TrashLog::getTrashInfo).toList();
        TrashOutput trashOutput = getTrashOutput(trashInfoList);

        return getEmptyTrashResultResponse(trashOutput, trashCanHistory.getTrashCanHistoryId());
    }
}
