package com.solution.green.dto;

import com.solution.green.entity.Category;
import com.solution.green.entity.SubCategories;
import lombok.*;

import javax.validation.constraints.NotNull;

public class SubCateDto {
    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class WithParent {
        @NotNull
        private Long subCategoryId;
        @NotNull
        private String subCategoryName;
        @NotNull
        private Long categoryId;
        @NotNull
        private String categoryName;
        public static WithParent fromEntity(SubCategories subCategory) {
            return WithParent.builder()
                    .subCategoryId(subCategory.getId())
                    .subCategoryName(subCategory.getName())
                    .categoryId(subCategory.getCategory().getId())
                    .categoryName(subCategory.getCategory().getName())
                    .build();
        }

    }
}
