package com.solution.green.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

public class MemCateDto {
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class Request {
        @NotNull
        private Long first;
        @NotNull
        private Long second;
        @NotNull
        private Long third;
        @NotNull
        private Long fourth;
    }
}
