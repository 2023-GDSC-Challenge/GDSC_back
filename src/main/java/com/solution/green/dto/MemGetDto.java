package com.solution.green.dto;

import com.solution.green.entity.Badge;
import com.solution.green.entity.Member;
import com.solution.green.entity.MemberGet;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

public class MemGetDto {

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class Title {
        @NotNull
        private Long id;
        @NotNull
        private String name;
        public static Title fromEntity(MemberGet memberGet) {
            return Title.builder()
                    .id(memberGet.getBadge().getId())
                    .name(memberGet.getBadge().getName())
                    .build();
        }
    }
}
