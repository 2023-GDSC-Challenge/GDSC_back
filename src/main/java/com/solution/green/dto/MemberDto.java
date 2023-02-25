package com.solution.green.dto;

import com.solution.green.entity.Member;
import lombok.*;

import javax.validation.constraints.NotNull;


public class MemberDto {
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class Login {
        @NotNull
        private String email;
        @NotNull
        private String password;
    }

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class Request {
        @NotNull
        private String nickname;
        @NotNull
        private String email;
        @NotNull
        private String password;
        @NotNull
        private String residence;
    }

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class Response {
        @NotNull
        private Long memberId;
        @NotNull
        private String nickname;
        @NotNull
        private String email;
        @NotNull
        private String password;
        @NotNull
        private String residence;

        public static Response fromEntity(Member member) {
            return Response.builder()
                    .memberId(member.getId())
                    .nickname(member.getNickname())
                    .email(member.getEmail())
                    .password(member.getPassword())
                    .residence(member.getTitle())
                    .build();
        }
    }
}