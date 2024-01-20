package com.bigbro.wwooss.v1.entity.trash.category;

import com.bigbro.wwooss.v1.entity.base.BaseEntity;
import com.bigbro.wwooss.v1.enumType.TrashType;
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

    @Comment("쓰레기 카테고리")
    @Column(name = "trash_category_name")
    @Enumerated(value = EnumType.STRING)
    private TrashType trashType;

    public static TrashCategory from(TrashType trashType) {
        return TrashCategory.builder()
                .trashType(trashType)
                .build();
    }
}
