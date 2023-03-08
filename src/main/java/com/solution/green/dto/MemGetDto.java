package com.solution.green.dto;

import com.solution.green.entity.Badge;
import com.solution.green.entity.MemberGet;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
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

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class List {
        @NotNull
        private Long memberGetId;
        @NotNull
        private Badge badge;
        private int choice;
        public static List fromEntity(MemberGet memberGet) {
            return List.builder()
                    .memberGetId(memberGet.getId())
                    .badge(memberGet.getBadge())
                    .choice(memberGet.getChoice())
                    .build();
        }
    }
}
