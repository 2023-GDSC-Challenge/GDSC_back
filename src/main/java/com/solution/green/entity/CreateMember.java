package com.solution.green.entity;

import com.google.firebase.database.annotations.NotNull;
import com.google.firebase.database.annotations.Nullable;
import lombok.*;

import javax.validation.constraints.Size;
import java.util.Date;


public class CreateMember {
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