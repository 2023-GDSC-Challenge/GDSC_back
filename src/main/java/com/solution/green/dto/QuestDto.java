package com.solution.green.dto;

import com.solution.green.entity.MemberDo;
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
    public static class ListView {
        @NotNull
        private Long id;
        @NotNull
        private String questName;
        private Long questId;
        @NotNull
        private SubCateDto.WithParent categoryDto;
        @NotNull
        private Integer reward;
        private String briefing;
        private Integer timeLimit;
        @Nullable
        @Setter
        private Integer challenger;

        public static ListView fromEntity(Quest quest) {
            return ListView.builder()
                    .id(quest.getId())
                    .questName(quest.getName())
                    .questId(quest.getId())
                    .categoryDto(SubCateDto.WithParent.fromEntity(quest.getSubCategory()))
                    .reward(quest.getReward())
                    .briefing(quest.getBriefing())
                    .timeLimit(quest.getTimeLimit())
                    .challenger(quest.getChallenger())
                    .build();
        }
        public static ListView fromEntity(MemberDo memberDo) {
            Quest quest = memberDo.getQuest();
            return fromEntity(quest);
        }
    }

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class DetailView {
        @NotNull
        private Long id;
        @NotNull
        private String questName;
        private Long questId;
        @NotNull
        private SubCateDto.WithParent categoryDto;
        @NotNull
        private Integer reward;
        private String briefing;
        private String information;
        private Integer timeLimit;
        @Nullable
        @Setter
        private Integer challenger;

        public static DetailView fromEntity(Quest quest) {
            return DetailView.builder()
                    .id(quest.getId())
                    .questName(quest.getName())
                    .questId(quest.getId())
                    .categoryDto(SubCateDto.WithParent.fromEntity(quest.getSubCategory()))
                    .reward(quest.getReward())
                    .briefing(quest.getBriefing())
                    .information(quest.getInformation())
                    .timeLimit(quest.getTimeLimit())
                    .challenger(quest.getChallenger())
                    .build();
        }
    }
}
