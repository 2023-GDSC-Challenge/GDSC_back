package com.solution.green.dto;

import com.solution.green.entity.Category;
import com.solution.green.entity.MemberCategory;
import lombok.*;

import javax.validation.constraints.NotNull;

public class CategoryDto {

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class Home {
        @NotNull
        private Long id;
        @NotNull
        private String name;
        private String icon;
        private int priority;
        private Double achieveRate;
        public static Home fromEntity(Category category) {
            return Home.builder()
                    .id(category.getId())
                    .name(category.getName())
                    .icon(category.getIcon())
                    .build();
        }

        public static Home fromEntity(MemberCategory memberCategory) {
            return Home.builder()
                    .id(memberCategory.getCategory().getId())
                    .name(memberCategory.getCategory().getName())
                    .icon(memberCategory.getCategory().getIcon())
                    .priority(memberCategory.getPriority())
                    .build();
        }
    }
}
