package com.bigbro.killbill.v1.service.trash.impl;

import com.bigbro.killbill.v1.domain.entity.trash.TrashCategory;
import com.bigbro.killbill.v1.domain.request.trash.category.TrashCategoryRequest;
import com.bigbro.killbill.v1.domain.response.trash.TrashCategoryResponse;
import com.bigbro.killbill.v1.repository.trash.category.TrashCategoryRepository;
import com.bigbro.killbill.v1.service.trash.category.TrashCategoryService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class TrashCategoryServiceImpl implements TrashCategoryService {

    private final TrashCategoryRepository trashCategoryRepository;

    @Override
    @Transactional(readOnly = true)
    public List<TrashCategoryResponse> getTrashCategories() {
        List<TrashCategory> trashCategoryResponses = trashCategoryRepository.findAll();
        return trashCategoryResponses.stream().map(TrashCategoryResponse::from).toList();
    }

    @Override
    @Transactional
    public void createTrashCategory(TrashCategoryRequest trashCategoryRequest) {
        trashCategoryRepository.save(TrashCategory.from(trashCategoryRequest));
    }
}
