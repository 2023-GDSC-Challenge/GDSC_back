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
        private int stance;
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
}
