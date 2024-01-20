package com.bigbro.wwooss.v1.service.trash.impl;

import com.bigbro.wwooss.v1.dto.TrashCanInfo;
import com.bigbro.wwooss.v1.dto.TrashCanInfoList;
import com.bigbro.wwooss.v1.entity.trash.can.TrashCan;
import com.bigbro.wwooss.v1.repository.trash.can.TrashCanRepository;
import com.bigbro.wwooss.v1.service.trash.can.TrashCanService;
import java.util.List;
import org.springframework.data.domain.Pageable;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class TrashCanServiceImpl implements TrashCanService {

    private final TrashCanRepository trashCanRepository;

    @Override
    @Transactional(readOnly = true)
    public TrashCanInfoList getTrashCanInfo(Pageable pageable) {
        Slice<TrashCan> trashCanAll = trashCanRepository.findAll(pageable);
        List<TrashCanInfo> trashCanInfoList = trashCanAll.getContent().stream().map(
                (trash) -> TrashCanInfo.of(trash.getTrashCanId(), trash.getLng(), trash.getLat(),
                        trash.getAddress(), trash.getTrashCategory())).toList();
        return TrashCanInfoList.of(trashCanAll.hasNext(), trashCanInfoList);
    }
}