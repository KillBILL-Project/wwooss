package com.bigbro.wwooss.v1.service.trash.impl;

import com.bigbro.wwooss.v1.dto.CarbonEmissionByTrashCategory;
import com.bigbro.wwooss.v1.dto.RefundByTrashCategory;
import com.bigbro.wwooss.v1.dto.ResultOfDiscardedTrash;
import com.bigbro.wwooss.v1.dto.response.trash.EmptyTrashResultResponse;
import com.bigbro.wwooss.v1.dto.response.trash.TrashCanHistoryListResponse;
import com.bigbro.wwooss.v1.dto.response.trash.TrashCanHistoryResponse;
import com.bigbro.wwooss.v1.entity.trash.can.TrashCanContents;
import com.bigbro.wwooss.v1.entity.trash.can.TrashCanHistory;
import com.bigbro.wwooss.v1.entity.trash.info.TrashInfo;
import com.bigbro.wwooss.v1.entity.user.User;
import com.bigbro.wwooss.v1.exception.DataNotFoundException;
import com.bigbro.wwooss.v1.repository.trash.can.TrashCanHistoryRepository;
import com.bigbro.wwooss.v1.repository.user.UserRepository;
import com.bigbro.wwooss.v1.response.WwoossResponseCode;
import com.bigbro.wwooss.v1.service.trash.can.TrashCanHistoryService;
import com.bigbro.wwooss.v1.service.trash.log.TrashLogService;
import com.bigbro.wwooss.v1.utils.TrashUtil;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class TrashCanHistoryServiceImpl implements TrashCanHistoryService {

    private final TrashCanHistoryRepository trashCanHistoryRepository;

    private final TrashLogService trashLogService;

    private final UserRepository userRepository;

    @Override
    @Transactional
    public EmptyTrashResultResponse createTrashCanHistory(List<TrashCanContents> trashCanContentsList, User user) {
        long totalRefund = 0;
        double totalCarbonEmission = 0;

        // 카테고리별 환급금 총합
        HashMap<String, Long> refundByCategory = new HashMap<>();
        // 카테고리별 탄소 배출량 총합
        HashMap<String, Double> carbonEmissionByCategory = new HashMap<>();

        for (TrashCanContents trashCanContents : trashCanContentsList) {
            TrashInfo trashInfo = trashCanContents.getTrashInfo();

            ResultOfDiscardedTrash discardedResult = TrashUtil.discardResult(trashCanContents.getTrashCount(),
                    trashInfo.getWeight(),
                    trashCanContents.getSize(),
                    trashInfo.getCarbonEmissionPerGram(),
                    trashInfo.getRefund()
            );

            // 카테고리별 탄소배출량, 환급금 합 계산
            refundByCategory.put(trashInfo.getName(), getTotalRefundByCategory(trashInfo.getName(), refundByCategory, discardedResult.getRefund()));
            carbonEmissionByCategory.put(trashInfo.getName(), getTotalCarbonEmissionByCategory(trashInfo.getName(), carbonEmissionByCategory, discardedResult.getCarbonEmission()));

            totalCarbonEmission += discardedResult.getCarbonEmission();
            totalRefund += discardedResult.getRefund();
        }

        TrashCanHistory savedTrashCanHistory = trashCanHistoryRepository.save(TrashCanHistory.of(totalCarbonEmission, totalRefund, user));
        trashLogService.updateTrashLogTrashHistory(savedTrashCanHistory, user);

        return getEmptyTrashResultResponse(refundByCategory, carbonEmissionByCategory, totalRefund, totalCarbonEmission);
    }

    private Long getTotalRefundByCategory(String trashName, HashMap<String, Long> refundByCategory, long refund) {
        Long totalRefundByCategory = refundByCategory.get(trashName);

        return (Objects.isNull(totalRefundByCategory) ? refund : (totalRefundByCategory + refund));
    }

    private Double getTotalCarbonEmissionByCategory(String trashName, HashMap<String, Double> carbonEmissionByCategory, double carbonEmission) {
        Double totalCarbonEmissionByCategory = carbonEmissionByCategory.get(trashName);

        return Objects.isNull(totalCarbonEmissionByCategory) ? carbonEmission : (totalCarbonEmissionByCategory + carbonEmission);
    }

    private EmptyTrashResultResponse getEmptyTrashResultResponse(HashMap<String, Long> refundByCategory,
                                                                 HashMap<String, Double> carbonEmissionByCategory,
                                                                 Long totalRefund,
                                                                 Double totalCarbonEmission) {
        List<RefundByTrashCategory> refundByTrashCategoryList = refundByCategory.keySet().stream()
                .map((categoryName) ->
                        RefundByTrashCategory.of(categoryName, refundByCategory.get(categoryName))
                ).toList();

        List<CarbonEmissionByTrashCategory> carbonEmissionByTrashCategoryList = carbonEmissionByCategory.keySet().stream()
                .map((categoryName) ->
                        CarbonEmissionByTrashCategory.of(categoryName, carbonEmissionByCategory.get(categoryName))
                ).toList();

        return EmptyTrashResultResponse.of(totalCarbonEmission, carbonEmissionByTrashCategoryList, totalRefund, refundByTrashCategoryList);
    }

    @Override
    @Transactional(readOnly = true)
    public TrashCanHistoryListResponse findTrashCanHistoryList(Long userId, String date, Pageable pageable) {
        User user = userRepository.findById(userId).orElseThrow(() -> new DataNotFoundException(WwoossResponseCode.NOT_FOUND_DATA, "존재하지 않는 유저입니다."));
        Slice<TrashCanHistory> trashCanHistories = trashCanHistoryRepository.findTrashCanHistoriesByUserAndDate(user, date, pageable);
        List<TrashCanHistoryResponse> trashCanHistoryResponseList = trashCanHistories.stream().map((TrashCanHistoryResponse::from)).toList();

        return TrashCanHistoryListResponse.of(trashCanHistories.hasNext(), trashCanHistoryResponseList);
    }
}
