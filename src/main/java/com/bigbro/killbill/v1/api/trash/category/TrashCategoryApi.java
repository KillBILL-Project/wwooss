package com.bigbro.killbill.v1.api.trash.category;

import com.bigbro.killbill.v1.common.KillBillResponse;
import com.bigbro.killbill.v1.common.KillBillResponseUtil;
import com.bigbro.killbill.v1.domain.request.trash.category.TrashCategoryRequest;
import com.bigbro.killbill.v1.domain.response.trash.TrashCategoryResponse;
import com.bigbro.killbill.v1.service.trash.category.TrashCategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/v1/trash-categories")
@RequiredArgsConstructor
public class TrashCategoryApi {

    private final TrashCategoryService trashCategoryService;

    @GetMapping
    public ResponseEntity<KillBillResponse<List<TrashCategoryResponse>>> getTrashCategories() {
        return KillBillResponseUtil.responseOkAddData(trashCategoryService.getTrashCategories());
    }

    /**
     * 내부 API
     * 카테고리 생성
     */
    @PostMapping
    public ResponseEntity<KillBillResponse<Void>> createTrashCategory(@RequestBody @Valid TrashCategoryRequest trashCategoryRequest) {
        trashCategoryService.createTrashCategory(trashCategoryRequest);

        return KillBillResponseUtil.responseOkNoData();
    }
}
