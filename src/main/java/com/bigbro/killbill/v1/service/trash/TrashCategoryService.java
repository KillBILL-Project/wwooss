package com.bigbro.killbill.v1.service.trash;

import com.bigbro.killbill.v1.domain.entity.trash.TrashCategoryEntity;
import com.bigbro.killbill.v1.domain.request.trash.TrashCategoryRequest;
import com.bigbro.killbill.v1.domain.response.trash.TrashCategoryResponse;
import com.bigbro.killbill.v1.repository.trash.TrashCategoryRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class TrashCategoryService {

    private final TrashCategoryRepository trashCategoryRepository;

    @Transactional(readOnly = true)
    public List<TrashCategoryResponse> getTrashCategories() {
        List<TrashCategoryEntity> trashCategoryResponses = trashCategoryRepository.findAll();
        return trashCategoryResponses.stream().map(TrashCategoryResponse::from).toList();
    }

    @Transactional
    public void createTrashCategory(TrashCategoryRequest trashCategoryRequest) {
        trashCategoryRepository.save(TrashCategoryEntity.from(trashCategoryRequest));
    }
}
