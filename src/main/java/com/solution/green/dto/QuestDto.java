package com.solution.green.dto;

import com.solution.green.entity.Quest;
import lombok.*;
import org.springframework.lang.Nullable;

import javax.validation.constraints.NotNull;

public class QuestDto {
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class Request {
        @NotNull
        private String questName;
        @NotNull
        private String subCateName;
        @NotNull
        private Long parentCategoryId;
        @NotNull
        private Integer reward;
        private String memo;
        private Integer timeLimit;
    }

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class Detail {
        @NotNull
        private Long id;
        @NotNull
        private String questName;
        private Long questId;
        @NotNull
        private SubCateDto.WithParent categoryDto;
        @NotNull
        private Integer reward;
        private String memo;
        private Integer timeLimit;
        @Nullable
        @Setter
        private Integer nowChallenger;

        public static Detail fromEntity(Quest quest) {
            return Detail.builder()
                    .id(quest.getId())
                    .questName(quest.getName())
                    .questId(quest.getId())
                    .categoryDto(SubCateDto.WithParent.fromEntity(quest.getSubCategory()))
                    .reward(quest.getReward())
                    .memo(quest.getMemo())
                    .timeLimit(quest.getTimeLimit())
                    .build();
        }
    }
}
