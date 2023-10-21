package com.bigbro.wwooss.v1.domain.entity.trash.category;

import com.bigbro.wwooss.v1.domain.entity.base.BaseEntity;
import com.bigbro.wwooss.v1.domain.request.trash.category.TrashCategoryRequest;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.Comment;

import javax.persistence.*;

@Entity
@Table(name = "trash_category",  uniqueConstraints={
        @UniqueConstraint(
                name = "trash_category_unique",
                columnNames = {
                        "trash_category_name",
                }
        )}
)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SuperBuilder
@Getter
public class TrashCategory extends BaseEntity {

    @Id
    @Column(name = "trash_category_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long trashCategoryId;

    @Comment("쓰레기 카테고리 이름")
    @Column(name = "trash_category_name")
    private String trashCategoryName;

    public static TrashCategory from(TrashCategoryRequest trashCategoryRequest) {
        return TrashCategory.builder()
                .trashCategoryName(trashCategoryRequest.getTrashCategoryName())
                .build();
    }
}
