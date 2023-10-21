package com.bigbro.wwooss.v1.service.trash.impl;

import com.bigbro.wwooss.v1.common.WwoossResponseCode;
import com.bigbro.wwooss.v1.domain.entity.trash.can.TrashCanContents;
import com.bigbro.wwooss.v1.domain.entity.trash.can.TrashCanHistory;
import com.bigbro.wwooss.v1.domain.entity.trash.info.TrashInfo;
import com.bigbro.wwooss.v1.domain.entity.user.User;
import com.bigbro.wwooss.v1.domain.response.trash.*;
import com.bigbro.wwooss.v1.exception.DataNotFoundException;
import com.bigbro.wwooss.v1.repository.trash.can.TrashCanHistoryRepository;
import com.bigbro.wwooss.v1.repository.user.UserRepository;
import com.bigbro.wwooss.v1.service.trash.can.TrashCanHistoryService;
import com.bigbro.wwooss.v1.service.trash.log.TrashLogService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
        final long WEIGHT_INTERVAL = 3;

        long totalRefund = 0;
        double totalCarbonEmission = 0;

        // 카테고리별 환급금 총합
        HashMap<String, Long> refundByCategory = new HashMap<>();
        // 카테고리별 탄소 배출량 총합
        HashMap<String, Double> carbonEmissionByCategory = new HashMap<>();

        for (TrashCanContents trashCanContents : trashCanContentsList) {
            TrashInfo trashInfo = trashCanContents.getTrashInfo();

            // size : 0 ~ 10
            // 버린 쓰레기 양 * 버린 쓰레기 사이즈의 무게
            Double amountOfTrash =
                    trashCanContents.getTrashCount() * ( trashInfo.getWeight() + WEIGHT_INTERVAL * (trashCanContents.getSize()));

            double carbonEmission = amountOfTrash * trashInfo.getCarbonEmissionPerGram();
            long refund = (long) (amountOfTrash * trashInfo.getRefund());

            // 카테고리별 탄소배출량, 환급금 합 계산
            refundByCategory.put(trashInfo.getName(), getTotalRefundByCategory(trashInfo.getName(), refundByCategory, refund));
            carbonEmissionByCategory.put(trashInfo.getName(), getTotalCarbonEmissionByCategory(trashInfo.getName(), carbonEmissionByCategory, carbonEmission));

            totalCarbonEmission += carbonEmission;
            totalRefund += refund;
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
    public TrashCanHistoryListResponse findTrashCanHistoryList(Long userId, Pageable pageable) {
        User user = userRepository.findById(userId).orElseThrow(() -> new DataNotFoundException(WwoossResponseCode.NOT_FOUND_DATA, "존재하지 않는 유저입니다."));
        Page<TrashCanHistory> trashCanHistories = trashCanHistoryRepository.findTrashCanHistoriesByUser(user, pageable);
        List<TrashCanHistoryResponse> trashCanHistoryResponseList = trashCanHistories.stream().map((TrashCanHistoryResponse::from)).toList();

        return TrashCanHistoryListResponse.of(trashCanHistories.hasNext(), trashCanHistoryResponseList);
    }
}
