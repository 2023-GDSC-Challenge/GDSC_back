package com.solution.green.dto;

import com.google.firebase.database.annotations.NotNull;
import lombok.*;

import javax.validation.constraints.Size;


public class MemberDto {
    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    @ToString
    public static class Request{
        @NotNull
        @Size(min = 3, max = 50, message = "memberId size must be 3~50")
        private String memberId;
        @NotNull
        private String name;
        @NotNull
        private String email;
        @NotNull
        private String password;
    }
    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    @ToString
    public static class Response{
        @NotNull
        @Size(min = 3, max = 50, message = "memberId size must be 3~50")
        private String memberId;
        @NotNull
        private String name;
        @NotNull
        private String email;
        @NotNull
        private String password;
    }
}