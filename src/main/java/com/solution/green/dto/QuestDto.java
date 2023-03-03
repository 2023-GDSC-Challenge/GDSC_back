package com.solution.green.dto;

import com.solution.green.entity.Quest;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

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
        @NotNull
        private SubCateDto.WithParent categoryDto;
        @NotNull
        private Integer reward;
        private String memo;
        private Integer timeLimit;

        public static Detail fromEntity(Quest quest) {
            return Detail.builder()
                    .id(quest.getId())
                    .questName(quest.getName())
                    .categoryDto(SubCateDto.WithParent.fromEntity(quest.getSubCategory()))
                    .reward(quest.getReward())
                    .memo(quest.getMemo())
                    .timeLimit(quest.getTimeLimit())
                    .build();
        }
    }
}
