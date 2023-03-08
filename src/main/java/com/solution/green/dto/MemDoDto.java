package com.solution.green.dto;

import com.solution.green.entity.MemberDo;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.util.Date;

public class MemDoDto {
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class My {
        @NotNull
        private Long memDoId;
        @NotNull
        private QuestDto.ListView questDto;
        private Date startDate;
        private Date dueDate;
        private Date leftTime; // 프론트에서 직접 계산
        private Boolean stance;
        private Double achieveRate; // 보류

        public static My fromEntity(MemberDo memberDo) {
            return My.builder()
                    .memDoId(memberDo.getId())
                    .questDto(QuestDto.ListView.fromEntity(memberDo.getQuest()))
                    .startDate(memberDo.getStartDate())
                    .dueDate(memberDo.getDueDate())
                    .stance(memberDo.getStance())
                    .build();
        }
    }
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class DetailView {
        // - 디테일 정보
        //    - 퀘스트 상세설명 attirbute 추가할 것 - DB
        @NotNull
        private Long memDoId;
        @NotNull
        private QuestDto.DetailView questDto;
        private Date startDate;
        private Date dueDate;
        private Date leftTime; // 프론트에서 직접 계산
        private Boolean stance;
        private Double achieveRate; // 보류

        public static DetailView fromEntity(MemberDo memberDo) {
            return DetailView.builder()
                    .memDoId(memberDo.getId())
                    .questDto(QuestDto.DetailView.fromEntity(memberDo.getQuest()))
                    .startDate(memberDo.getStartDate())
                    .dueDate(memberDo.getDueDate())
                    .stance(memberDo.getStance())
                    .build();
        }
    }
}
